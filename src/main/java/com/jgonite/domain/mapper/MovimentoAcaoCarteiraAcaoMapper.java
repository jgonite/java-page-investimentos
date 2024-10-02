package com.jgonite.domain.mapper;

import java.math.BigDecimal;

import org.springframework.stereotype.Component;

import com.jgonite.adapter.postgres.model.MovimentoAcoesModel;
import com.jgonite.domain.dto.CarteiraAcaoDTO;
import com.jgonite.domain.entity.CarteiraAcaoEntity;
import com.jgonite.domain.util.StringUtils;

@Component
public class MovimentoAcaoCarteiraAcaoMapper {
	
	public CarteiraAcaoEntity toCarteiraAcaoEntity(MovimentoAcoesModel movimento) {
		CarteiraAcaoEntity novaAcao = new CarteiraAcaoEntity();
		BigDecimal valorInvestidoDesinvestido = movimento.getValorBruto();
		Integer numeroAcoes = movimento.getNumeroAcoes();
		novaAcao.setAcao(movimento.getNomeAcao());
		novaAcao.setQtdAcoes(numeroAcoes);
		novaAcao.setValorInvestidoDesinvestido(valorInvestidoDesinvestido);
		return novaAcao;
	}
	
	public CarteiraAcaoDTO toDTO(CarteiraAcaoEntity entity) {
		CarteiraAcaoDTO dto = new CarteiraAcaoDTO();
		dto.setAcao(entity.getAcao());
		dto.setQtdAcoes(String.valueOf(entity.getQtdAcoes()));
		dto.setValorInvestido(StringUtils.bigDecimalParaMoeda(entity.getValorInvestidoDesinvestido()));
		dto.setValorAtual(StringUtils.bigDecimalParaMoeda(entity.getValorAtual()));
		dto.setValorProventos(StringUtils.bigDecimalParaMoeda(entity.getValorProventos()));
		dto.setPrecoMedio(StringUtils.bigDecimalParaMoeda(entity.getPrecoMedio()));
		dto.setROI(StringUtils.bigDecimalParaPorcentagem(entity.getROI()));
		dto.setPercCarteira(StringUtils.bigDecimalParaPorcentagem(entity.getPercCarteira()));
		return dto;
	}

}
