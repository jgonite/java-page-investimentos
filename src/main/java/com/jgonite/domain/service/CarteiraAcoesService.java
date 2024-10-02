package com.jgonite.domain.service;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jgonite.adapter.postgres.model.DividendoAcoesModel;
import com.jgonite.adapter.postgres.model.MovimentoAcoesModel;
import com.jgonite.adapter.postgres.repository.DividendoRepository;
import com.jgonite.adapter.postgres.repository.MovimentosRepository;
import com.jgonite.domain.dto.CarteiraAcaoDTO;
import com.jgonite.domain.dto.CarteiraAcoesDTO;
import com.jgonite.domain.entity.CarteiraAcaoEntity;
import com.jgonite.domain.mapper.MovimentoAcaoCarteiraAcaoMapper;
import com.jgonite.domain.util.StringUtils;

@Service
public class CarteiraAcoesService {
	
	@Autowired
	private MovimentosRepository movimentoRepository;
	
	@Autowired
	private DividendoRepository dividendosRepository;
	
	@Autowired
	private MovimentoAcaoCarteiraAcaoMapper mapper;
	
	@Autowired
	private FundamentusService fundamentusService;

	public CarteiraAcoesDTO calcularCarteira() {
		List<MovimentoAcoesModel> movimentos = this.movimentoRepository.findAll();
		
		BigDecimal valorTotalInvestido = BigDecimal.ZERO;
		Map<String, CarteiraAcaoEntity> mapDeAcoesCarteiraEntity = new HashMap<>();
		for (MovimentoAcoesModel movimento : movimentos) {
			CarteiraAcaoEntity cae = mapper.toCarteiraAcaoEntity(movimento);
			if (mapDeAcoesCarteiraEntity.get(movimento.getNomeAcao()) == null) {
				mapDeAcoesCarteiraEntity.put(movimento.getNomeAcao(), cae);
			} else {
				mapDeAcoesCarteiraEntity.get(movimento.getNomeAcao()).incrementValorInvestidoDesinvestido(cae.getValorInvestidoDesinvestido());
				mapDeAcoesCarteiraEntity.get(movimento.getNomeAcao()).incrementQtdAcoes(cae.getQtdAcoes());
			}
			valorTotalInvestido = valorTotalInvestido.add(
					cae.getValorInvestidoDesinvestido()
					);
		}
		
		Collection<CarteiraAcaoEntity> listDeAcoesCarteiraEntity = mapDeAcoesCarteiraEntity.values();
		BigDecimal valorTotalCarteira = BigDecimal.ZERO;
		BigDecimal valorTotalProventos = BigDecimal.ZERO;
		List<DividendoAcoesModel> dividendosTodos = dividendosRepository.findAll();
		for (var acao : listDeAcoesCarteiraEntity) {
			// adquire a cotação atual e faz valor atual = qtd acoes * cotacao atual
			try {
				acao.setValorAtual(fundamentusService.pegarCotacao(
						acao.getAcao()).multiply(BigDecimal.valueOf(acao.getQtdAcoes())));
			} catch (IOException e) {
				acao.setValorAtual(BigDecimal.ZERO);
			}
			
			var valorProventos = BigDecimal.valueOf(dividendosTodos.stream()
					.filter(div -> div.getNomeAcao().equalsIgnoreCase(acao.getAcao()))
					.reduce(0f, (subtotal, dividendoAcao) -> subtotal + dividendoAcao.getValorRecebido().floatValue(), Float::sum));
			acao.setValorProventos(valorProventos);
			valorTotalProventos = valorTotalProventos.add(valorProventos);
			
			if (BigDecimal.ZERO.compareTo( BigDecimal.valueOf(acao.getQtdAcoes())) == 0 ) {
				acao.setPrecoMedio(BigDecimal.ZERO);
			} else {
				acao.setPrecoMedio(
						acao.getValorInvestidoDesinvestido().divide(BigDecimal.valueOf(acao.getQtdAcoes()), RoundingMode.HALF_UP)
						);
			}
			if (BigDecimal.ZERO.compareTo( acao.getValorAtual() ) == 0 ) {
				
			} else {
				acao.setROI(
						acao.getValorAtual().add(acao.getValorProventos())
						.divide(acao.getValorInvestidoDesinvestido(), RoundingMode.HALF_UP) .subtract(BigDecimal.ONE)
						);
			}
			valorTotalCarteira = valorTotalCarteira.add(acao.getValorAtual());
		}
		valorTotalInvestido = valorTotalInvestido.subtract(valorTotalProventos);
		
		// % carteira:
		CarteiraAcoesDTO resposta = new CarteiraAcoesDTO();
		resposta.setValorInvestido(StringUtils.bigDecimalParaMoeda(valorTotalInvestido));
		resposta.setValorAtual(StringUtils.bigDecimalParaMoeda(valorTotalCarteira));
		if (valorTotalCarteira.compareTo(BigDecimal.ZERO) != 0) {
			for (var acao : listDeAcoesCarteiraEntity) {
				acao.setPercCarteira(
						acao.getValorAtual().divide(valorTotalCarteira, RoundingMode.HALF_UP)
						) ;
			};
		}
		if (valorTotalInvestido.compareTo(BigDecimal.ZERO) != 0) {
			resposta.setROI(StringUtils.bigDecimalParaPorcentagem(
					valorTotalCarteira.divide(valorTotalInvestido, RoundingMode.HALF_UP) .subtract(BigDecimal.ONE)
					));
		} else {
			resposta.setROI(StringUtils.bigDecimalParaPorcentagem(BigDecimal.ZERO));
		}
		// mapear como DTO:
		List<CarteiraAcaoDTO> carteiraAcoes =  listDeAcoesCarteiraEntity.stream()
				.map(acao->mapper.toDTO(acao))
				.collect(Collectors.toList());
		resposta.setCarteiraAcoes(carteiraAcoes);
		return resposta;
		
	}

}
