package com.jgonite.domain.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@ToString(callSuper = true)
public class InvesteSiteExtracaoRankingRoicEvEbitdaDTO extends InvesteSiteExtracaoRankingDTO {
	
	private String roic;
	private String rankingRoic;
	private String evEbitda;
	private String rankingEvEbitda;
	
}
