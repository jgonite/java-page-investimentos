package com.jgonite.domain.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@ToString(callSuper = true)
public class InvesteSiteExtracaoRankingRoePlDTO extends InvesteSiteExtracaoRankingDTO {
	
	private String roe;
	private String rankingRoe;
	private String pl;
	private String rankingPl;
	
}
