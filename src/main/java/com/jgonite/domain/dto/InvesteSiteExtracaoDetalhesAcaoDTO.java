package com.jgonite.domain.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class InvesteSiteExtracaoDetalhesAcaoDTO {
	
	private String ticker;
	private String nome;
	private InvesteSiteExtracaoDetalhesAcaoGraficoDTO graficoRoic;
	private InvesteSiteExtracaoDetalhesAcaoGraficoDTO graficoMargemLiquida;
	private InvesteSiteExtracaoDetalhesAcaoGraficoDTO graficoMargemEbitda;
	private InvesteSiteExtracaoDetalhesAcaoGraficoDTO graficoEndividamento;

}
