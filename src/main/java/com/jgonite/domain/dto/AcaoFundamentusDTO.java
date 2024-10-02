package com.jgonite.domain.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class AcaoFundamentusDTO {
	
	private String papel;
	private String cotacao;
	private String pl;
	private String evEbit;
	private String roic;
	private String liq2meses;

	@Override
	public String toString() {
		return "AcaoIndicadoresDTO [papel=" + papel + ", cotacao=" + cotacao + ", pl=" + pl + ", evEbit=" + evEbit
				+ ", roic=" + roic + ", liq2meses=" + liq2meses + "]";
	}
	
	

}
