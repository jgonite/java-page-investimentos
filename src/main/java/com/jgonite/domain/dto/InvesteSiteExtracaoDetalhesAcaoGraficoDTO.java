package com.jgonite.domain.dto;

import java.math.BigDecimal;
import java.util.Map;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class InvesteSiteExtracaoDetalhesAcaoGraficoDTO {
	
	private String titulo;
	private Map<String, BigDecimal> pontos;

}
