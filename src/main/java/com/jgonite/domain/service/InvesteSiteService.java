package com.jgonite.domain.service;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.jgonite.adapter.postgres.model.IndicadoresInvesteSiteModel;
import com.jgonite.adapter.postgres.model.compositekey.IndicadoresInvesteSiteCompositeKey;
import com.jgonite.adapter.postgres.repository.IndicadoresInvesteSiteRepository;
import com.jgonite.domain.dto.InvesteSiteExtracaoDetalhesAcaoDTO;
import com.jgonite.domain.dto.InvesteSiteExtracaoDetalhesAcaoGraficoDTO;
import com.jgonite.domain.dto.InvesteSiteExtracaoRankingDTO;
import com.jgonite.domain.dto.InvesteSiteExtracaoRankingRoePlDTO;
import com.jgonite.domain.dto.InvesteSiteExtracaoRankingRoicEvEbitdaDTO;
import com.jgonite.domain.util.InvesteSiteUtils;
import com.jgonite.domain.util.MathUtils;
import com.jgonite.port.http.out.InvesteSitePort;
import com.jgonite.port.http.out.dto.SessaoPrincipaisIndicadoresDTO;

@Component
public class InvesteSiteService {

	private static final Logger logger = LoggerFactory.getLogger(InvesteSiteService.class);

	@Value("${url.investe-site.ranking-roic-evebtida-capital-intensivo}")
	private String urlRankingCapitalIntensivo;
	@Value("${url.investe-site.ranking-roic-evebtida-utilidades}")
	private String urlRankingCapitalUtilidades;
	@Value("${url.investe-site.ranking-roe-pl-financeiras}")
	private String urlRankingFincaneiras;
	
	@Autowired
	private InvesteSitePort investSitePort;
	
	@Autowired
	private IndicadoresInvesteSiteRepository indicadoresInvesteSiteRepository;

	private Long VOLUME_DE_CORTE = 25000L;

	public List<InvesteSiteExtracaoRankingRoicEvEbitdaDTO> extrairRankingCapitalIntesivo() throws IOException {
		var table = investSitePort.extrairRanking(InvesteSiteUtils.substituirOdateNaUrl(urlRankingCapitalIntensivo));
		List<InvesteSiteExtracaoRankingRoicEvEbitdaDTO> listaDeAcoes = extrairRoicEvEbitda( Jsoup.parse(table) );
		listaDeAcoes = filtrarVolume(listaDeAcoes, VOLUME_DE_CORTE);
		listaDeAcoes = removerDuplicatas(listaDeAcoes);
		listaDeAcoes = ordernarPorSomaRanking(listaDeAcoes);
		return listaDeAcoes;
	}

	public List<InvesteSiteExtracaoRankingRoicEvEbitdaDTO> extrairRankingUtilidades() throws IOException {
		var table = investSitePort.extrairRanking(InvesteSiteUtils.substituirOdateNaUrl(urlRankingCapitalUtilidades));
		List<InvesteSiteExtracaoRankingRoicEvEbitdaDTO> listaDeAcoes = extrairRoicEvEbitda( Jsoup.parse(table) );
		listaDeAcoes = filtrarVolume(listaDeAcoes, VOLUME_DE_CORTE);
		listaDeAcoes = removerDuplicatas(listaDeAcoes);
		listaDeAcoes = ordernarPorSomaRanking(listaDeAcoes);
		return listaDeAcoes;
	}

	public List<InvesteSiteExtracaoRankingRoePlDTO> extrairRankingFinanceiras() throws IOException {
		var table = investSitePort.extrairRanking(InvesteSiteUtils.substituirOdateNaUrl(urlRankingFincaneiras));
		List<InvesteSiteExtracaoRankingRoePlDTO> listaDeAcoes = extrairRoePL( Jsoup.parse(table) );
		listaDeAcoes = filtrarVolume(listaDeAcoes, VOLUME_DE_CORTE);
		listaDeAcoes = removerDuplicatas(listaDeAcoes);
		listaDeAcoes = ordernarPorSomaRanking(listaDeAcoes);
		return listaDeAcoes;
	}
	
	public InvesteSiteExtracaoDetalhesAcaoDTO extrairDetalhesAcao(String ticker) throws IOException, InterruptedException, ExecutionException {
		InvesteSiteExtracaoDetalhesAcaoDTO responseDTO = new InvesteSiteExtracaoDetalhesAcaoDTO();
		responseDTO.setTicker(ticker);		
		//Verifica quais são os Tickers das financeiras (pois elas são ROE, não ROIC)
		Set<String> tickersFinanceiras = this.extrairRankingFinanceiras().stream().map(x->x.getTicker()).map(x->x.replaceAll("[0-9]", "")).collect(Collectors.toSet());
		// Entra na página de principais indicadores para o Ticker, cria a sessão e pega os odates relevantes
		SessaoPrincipaisIndicadoresDTO sessao = investSitePort.criarSessaoPrincipaisIndicadores(ticker);
		Element tablePrincipaisIndicadores = Jsoup.parse(sessao.getOuterHml());
		
		if (tickersFinanceiras.contains(ticker.replaceAll("[0-9]", ""))) {
			
		} else {
			// pega os Odates para RoicMagemEndiviamento do Html dos PrincipaisINdicadores
			Element tableRoicMargensEndividamentoAsElement = tablePrincipaisIndicadores.select("table#tabela_resumo_empresa_margens_retornos").first();
			Elements options = tableRoicMargensEndividamentoAsElement
					.selectFirst("thead")
					.select("th").get(1).select("select.data_tabela")
					.select("option");
			List<String> odatesRoicMagemEndividamento = new ArrayList<>();
			for (Element option : options) {
	            String value = option.attr("value");
	            odatesRoicMagemEndividamento.add(value);
	        }
			// Gráfico Roic, Margens e Endividamento
			InvesteSiteExtracaoDetalhesAcaoGraficoDTO graficoRoic = new InvesteSiteExtracaoDetalhesAcaoGraficoDTO();
			Map<String, BigDecimal> pontosRoic = new HashMap<String, BigDecimal>();
			graficoRoic.setTitulo("ROIC");
			graficoRoic.setPontos(pontosRoic);
			InvesteSiteExtracaoDetalhesAcaoGraficoDTO graficoMargemLiquida = new InvesteSiteExtracaoDetalhesAcaoGraficoDTO();
			Map<String, BigDecimal> pontosMargemLiquida= new HashMap<String, BigDecimal>();
			graficoMargemLiquida.setTitulo("Margem Líquida");
			graficoMargemLiquida.setPontos(pontosMargemLiquida);
			InvesteSiteExtracaoDetalhesAcaoGraficoDTO graficoMargemEbitda = new InvesteSiteExtracaoDetalhesAcaoGraficoDTO();
			Map<String, BigDecimal> pontosMargemEbitda= new HashMap<String, BigDecimal>();
			graficoMargemEbitda.setTitulo("Margem Ebitda");
			graficoMargemEbitda.setPontos(pontosMargemEbitda);
			InvesteSiteExtracaoDetalhesAcaoGraficoDTO graficoEndividamento= new InvesteSiteExtracaoDetalhesAcaoGraficoDTO();
			Map<String, BigDecimal> pontosEndividamento = new HashMap<String, BigDecimal>();
			graficoEndividamento.setTitulo("Div. Liquida / Ebitda");
			graficoEndividamento.setPontos(pontosEndividamento);
			for (String odate : odatesRoicMagemEndividamento) {
				var chaveDeCachePermanente = IndicadoresInvesteSiteCompositeKey.builder().odate(odate).nmAcao(ticker).build();
				if (indicadoresInvesteSiteRepository.existsById(chaveDeCachePermanente)) {
					IndicadoresInvesteSiteModel cachePermanente = indicadoresInvesteSiteRepository.findById(chaveDeCachePermanente).get();
					if  (cachePermanente.getValorRoic() != null) pontosRoic.put(odate, cachePermanente.getValorRoic());
					if  (cachePermanente.getValorMargemLiquida() != null) pontosMargemLiquida.put(odate, cachePermanente.getValorMargemLiquida());
					if  (cachePermanente.getValorMargemEbit() != null) pontosMargemEbitda.put(odate, cachePermanente.getValorMargemEbit());
					if  (cachePermanente.getValorEndividamento() != null) pontosEndividamento.put(odate, cachePermanente.getValorEndividamento());
				} else {
					Element asHtml = Jsoup.parse(investSitePort.extrairRoicMargensEndividamento(ticker, odate, sessao));
					Elements as = asHtml.select("a");
					IndicadoresInvesteSiteModel cachePermanente = new IndicadoresInvesteSiteModel();
					cachePermanente.setChavePrimaria(IndicadoresInvesteSiteCompositeKey.builder().odate(odate).nmAcao(ticker).build());
					cachePermanente.setChavePrimaria(IndicadoresInvesteSiteCompositeKey.builder().odate(odate).nmAcao(ticker).build());
					
					if (as != null && as.size() > 0) {
						BigDecimal roic = null;
						BigDecimal margemLiquida = null;
						BigDecimal margemEbtida = null;
						BigDecimal endividamento = null;
						if (as.size()>1) {
							roic = MathUtils.valorPorcentagemComoNumerico(as.get(1).text().split(" ")[0]);
						}
						if (as.size()>7) {
							margemLiquida = MathUtils.valorPorcentagemComoNumerico(as.get(7).text().split(" ")[0]);
						}
						if (as.size() > 9) {
							margemEbtida = MathUtils.valorPorcentagemComoNumerico(as.get(9).text().split(" ")[0]);
						}
						if (as.size() > 13) {
							endividamento = MathUtils.valorStringComoNumerico(as.get(13).text().split(" ")[0]);
						}
						if  (roic != null) pontosRoic.put(odate, roic);
						if  (margemLiquida != null) pontosMargemLiquida.put(odate, margemLiquida);
						if  (margemEbtida != null) pontosMargemEbitda.put(odate, margemEbtida);
						if  (endividamento != null) pontosEndividamento.put(odate, endividamento);
						// salvar cache permanente:
						cachePermanente.setValorRoic(roic);
						cachePermanente.setValorMargemLiquida(margemLiquida);
						cachePermanente.setValorMargemEbit(margemEbtida);
						cachePermanente.setValorEndividamento(endividamento);
					} else { 
						logger.info(String.format("Não há dados para extrair de %s no odate %s", ticker, odate));
					}
					cachePermanente.setDataUltimaAtualizacao(LocalDateTime.now());
					indicadoresInvesteSiteRepository.save(cachePermanente);
					logger.info(String.format("Foi salvo cache permanente para %S com odate %s", ticker, odate));
				}

			}
			responseDTO.setGraficoRoic(graficoRoic);
			responseDTO.setGraficoMargemLiquida(graficoMargemLiquida);
			responseDTO.setGraficoMargemEbitda(graficoMargemEbitda);
			responseDTO.setGraficoEndividamento(graficoEndividamento);
		}
		return responseDTO;
	}

	private static List<InvesteSiteExtracaoRankingRoicEvEbitdaDTO> extrairRoicEvEbitda(Element table) {
		boolean header = true;
		List<InvesteSiteExtracaoRankingRoicEvEbitdaDTO> list = new ArrayList<InvesteSiteExtracaoRankingRoicEvEbitdaDTO>();
		for (Element row : table.select("tr")) {
			if (header) {
				header = false;
				continue;
			}
			Elements column = row.select("td");
			InvesteSiteExtracaoRankingRoicEvEbitdaDTO dto = new InvesteSiteExtracaoRankingRoicEvEbitdaDTO();
			dto.setTicker(column.get(1).select("a").text());
			dto.setNome(column.get(2).text());
			dto.setDataUltimoDRE(column.get(5).text());
			dto.setRoic(column.get(7).text());
			dto.setRankingRoic(column.get(8).text());
			dto.setEvEbitda(column.get(9).text());
			dto.setRankingEvEbitda(column.get(10).text());
			dto.setSomaRanking(column.get(11).text());
			dto.setVolume90Dias(pegarVolumeFinanceiro(column.get(12).text()));
			list.add(dto);
		}
		return list;
	}

	private static List<InvesteSiteExtracaoRankingRoePlDTO> extrairRoePL(Element table) {
		boolean header = true;
		List<InvesteSiteExtracaoRankingRoePlDTO> list = new ArrayList<InvesteSiteExtracaoRankingRoePlDTO>();
		for (Element row : table.select("tr")) {
			if (header) {
				header = false;
				continue;
			}
			Elements column = row.select("td");
			InvesteSiteExtracaoRankingRoePlDTO dto = new InvesteSiteExtracaoRankingRoePlDTO();
			dto.setTicker(column.get(1).select("a").text());
			dto.setNome(column.get(2).text());
			dto.setDataUltimoDRE(column.get(5).text());
			dto.setRoe(column.get(7).text());
			dto.setRankingRoe(column.get(8).text());
			dto.setPl(column.get(9).text());
			dto.setRankingPl(column.get(10).text());
			dto.setSomaRanking(column.get(11).text());
			dto.setVolume90Dias(pegarVolumeFinanceiro(column.get(12).text()));
			list.add(dto);
		}
		return list;
	}

	private static Long pegarVolumeFinanceiro(String s) {
		return Long.parseLong(s.replaceAll("\\.", ""));
	}

	private static <T extends InvesteSiteExtracaoRankingDTO> List<T> filtrarVolume(List<T> ranking,
			Long volumeDeCorte) {
		List<T> rankingFiltrado = ranking.stream().filter(x -> x.getVolume90Dias() >= volumeDeCorte)
				.collect(Collectors.toList());
		return rankingFiltrado;
	}

	private static <T extends InvesteSiteExtracaoRankingDTO> List<T> removerDuplicatas(List<T> ranking) {
		Map<String, T> acoesJaLidas = new HashMap<>();
		for (T acao : ranking) {
			String tickerSemClasse = acao.getTicker().replaceAll("[0-9]", "");
			if (acoesJaLidas.keySet().contains(tickerSemClasse)) {
				if (acao.getVolume90Dias() >= acoesJaLidas.get(tickerSemClasse).getVolume90Dias()) {
					acoesJaLidas.put(tickerSemClasse, acao);
				}
			} else {
				acoesJaLidas.put(tickerSemClasse, acao);
			}
		}
		List<T> rankingFiltrado = acoesJaLidas.values().stream().collect(Collectors.toList());
		return rankingFiltrado;
	}

	private static <T extends InvesteSiteExtracaoRankingDTO> List<T> ordernarPorSomaRanking(List<T> ranking) {
		List<T> rankingOrdernado = ranking.stream().sorted(new Comparator<T>() {
			@Override
			public int compare(T o1, T o2) {
				return Integer.parseInt(o1.getSomaRanking()) - Integer.parseInt(o2.getSomaRanking());
			}
		}).collect(Collectors.toList());
		int tamanho = rankingOrdernado.size();
		for (int i = 0; i < tamanho; i++) {
			rankingOrdernado.get(i).setPosicaoFinal(String.valueOf(i + 1));
		}
		return rankingOrdernado;
	}

}
