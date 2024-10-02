package com.jgonite.domain.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class InvesteSiteExtracaoRankingDTO {
	
	private String posicaoFinal;
	private String ticker;
	private String nome;
	private String dataUltimoDRE;
	private String somaRanking;
	private Long volume90Dias; 

}
