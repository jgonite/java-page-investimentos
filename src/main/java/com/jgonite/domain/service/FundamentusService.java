package com.jgonite.domain.service;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.jgonite.adapter.postgres.model.AcaoIndicadoresModel;
import com.jgonite.domain.dto.AcaoFundamentusDTO;
import com.jgonite.domain.dto.AcaoIndicadoresDTO;
import com.jgonite.domain.dto.RankingMagicFormulaDTO;
import com.jgonite.domain.mapper.AcaoIndicadoresMapper;
import com.jgonite.domain.mapper.AcaoIndicadoresModelRankingMagicFormulaMapper;

@Component
public class FundamentusService {

	private static LocalDateTime instanteUltimoCache;
	private static Map<String, BigDecimal> cotacaoCache = new HashMap<>();
	@Autowired
	private AcaoIndicadoresModelRankingMagicFormulaMapper magicFormulaMapper;

	public List<String> listarAcoesDeUmSetor(String setor) throws IOException {
		Document doc = Jsoup.connect("https://www.fundamentus.com.br/resultado.php?setor=".concat(setor)).get();
		Element table = doc.select("table#resultado").first();
		boolean header = true;
		List<AcaoFundamentusDTO> listaDeAcoes = new ArrayList<>();
		for (Element row : table.select("tr")) {
			if (header) {
				header = false;
				continue;
			}
			Elements column = row.select("td");
			AcaoFundamentusDTO dto = new AcaoFundamentusDTO();
			dto.setPapel(limparTextoExtraido(column.get(0).select("a").text()));
			listaDeAcoes.add(dto);
		}
		return listaDeAcoes.stream().map(x -> x.getPapel()).sorted(Comparator.naturalOrder())
				.collect(Collectors.toList());
	}

	public BigDecimal pegarCotacao(String papel) throws IOException {
		refreshCotacaoCache();
		return Optional.ofNullable(cotacaoCache.get(papel)).orElse(BigDecimal.ZERO);
	}

	private void refreshCotacaoCache() throws IOException {
		LocalDateTime cincoMinutosAtras = LocalDateTime.now().minusMinutes(5);
		if (instanteUltimoCache == null || cincoMinutosAtras.compareTo(instanteUltimoCache) > 0) {
			cotacaoCache.clear();
			Document doc = Jsoup.connect("https://www.fundamentus.com.br/resultado.php").get();
			Element table = doc.select("table#resultado").first();
			boolean header = true;
			for (Element row : table.select("tr")) {
				if (header) {
					header = false;
					continue;
				}
				Elements column = row.select("td");
				cotacaoCache.put(limparTextoExtraido(column.get(0).select("a").text()),
						new BigDecimal(limparTextoExtraido(column.get(1).text())));
			}
			instanteUltimoCache = LocalDateTime.now();
		}
	}

	private String limparTextoExtraido(String texto) {
		return texto.replaceAll("\\.", "").replace(",", ".");
	}

	public List<RankingMagicFormulaDTO> getRanking() throws IOException {
		List<AcaoIndicadoresModel> listaAcoesModel = this.extract();
		// Tirar as ações cujo liquidez em 2 meses, seja menor que 100 mil.
		listaAcoesModel = listaAcoesModel.stream()
				.filter(x -> x.getLiq2meses().compareTo(new BigDecimal("25000")) >= 0).collect(Collectors.toList());
		listaAcoesModel = listaAcoesModel.stream().filter(x -> x.getEvEbit().compareTo(BigDecimal.ZERO) > 0)
				.collect(Collectors.toList());
		// Remover seguradoras, bancos e ações de empresas de energia.
		List<String> acoesSeguros = listarAcoesDeUmSetor("31");
		List<String> acoesEnergia = listarAcoesDeUmSetor("14");
		List<String> acoesBancos = listarAcoesDeUmSetor("20");
		List<String> exclusoes = acoesSeguros;
		exclusoes.addAll(acoesEnergia);
		exclusoes.addAll(acoesBancos);
		listaAcoesModel = listaAcoesModel.stream().filter(x -> !exclusoes.contains(x.getPapel()))
				.collect(Collectors.toList());
		// Filtrar as ações repetidas, mantendo apenas a que tem volume maior;
		Map<String, AcaoIndicadoresModel> papeisJaLidos = new HashMap<>();
		for (var acao : listaAcoesModel) {
			String papel = acao.getPapel().replaceAll("[0-9]", "");
			if (papeisJaLidos.keySet().contains(papel)) {
				if (acao.getLiq2meses().compareTo(papeisJaLidos.get(papel).getLiq2meses()) >= 0) {
					papeisJaLidos.put(papel, acao);
				}
			} else {
				papeisJaLidos.put(papel, acao);
			}
		}
		listaAcoesModel = papeisJaLidos.values().stream().collect(Collectors.toList());
		int tamanho = listaAcoesModel.size();
		// ranking de evEbit
		listaAcoesModel.sort(new Comparator<AcaoIndicadoresModel>() {
			@Override
			public int compare(AcaoIndicadoresModel a1, AcaoIndicadoresModel a2) {
				return a1.getEvEbit().compareTo(a2.getEvEbit());
			}
		});
		for (int i = 0; i < tamanho; i++) {
			listaAcoesModel.get(i).setPosicaoEvEbit(i + 1);
		}
		// ranking de roic
		listaAcoesModel.sort(new Comparator<AcaoIndicadoresModel>() {
			@Override
			public int compare(AcaoIndicadoresModel a1, AcaoIndicadoresModel a2) {
				return a2.getRoic().compareTo(a1.getRoic());
			}
		});
		for (int i = 0; i < tamanho; i++) {
			listaAcoesModel.get(i).setPosicaoRoic(i + 1);
			listaAcoesModel.get(i).setPosicaoFinal(
					listaAcoesModel.get(i).getPosicaoEvEbit() + listaAcoesModel.get(i).getPosicaoRoic());
		}
		// ranking por posicao final
		listaAcoesModel.sort(new Comparator<AcaoIndicadoresModel>() {
			@Override
			public int compare(AcaoIndicadoresModel a1, AcaoIndicadoresModel a2) {
				return a1.getPosicaoFinal() - a2.getPosicaoFinal();
			}
		});
		for (int i = 0; i < tamanho; i++) {
			listaAcoesModel.get(i).setPosicaoFinal(i + 1);
		}
		return listaAcoesModel.stream().map(x-> magicFormulaMapper.toRankingMagicFormulaDTO(x)).collect(Collectors.toList());
	}

	private List<AcaoIndicadoresModel> extract() throws IOException {
		Document doc = Jsoup.connect("https://www.fundamentus.com.br/resultado.php").get();
		Element table = doc.select("table#resultado").first();
		boolean header = true;
		List<AcaoIndicadoresDTO> listaDeAcoes = new ArrayList<>();
		for (Element row : table.select("tr")) {
			if (header) {
				header = false;
				continue;
			}
			Elements column = row.select("td");
			AcaoIndicadoresDTO dto = new AcaoIndicadoresDTO();
			dto.setPapel(limparTextoExtraido(column.get(0).select("a").text()));
			dto.setCotacao(limparTextoExtraido(column.get(1).text()));
			dto.setPl(limparTextoExtraido(column.get(2).text()));
			dto.setEvEbit(limparTextoExtraido(column.get(10).text()));
			dto.setRoic(limparTextoExtraido(column.get(15).text()));
			dto.setLiq2meses(limparTextoExtraido(column.get(17).text()));
			listaDeAcoes.add(dto);
		}
		AcaoIndicadoresMapper mapper = new AcaoIndicadoresMapper();
		List<AcaoIndicadoresModel> listaAcoesTratadas = listaDeAcoes.stream().map(x -> mapper.toModel(x))
				.collect(Collectors.toList());

		return listaAcoesTratadas;
	}

}
