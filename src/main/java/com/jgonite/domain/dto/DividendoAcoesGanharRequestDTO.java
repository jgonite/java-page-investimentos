package com.jgonite.domain.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Positive;

public class DividendoAcoesGanharRequestDTO {

	@NotNull
	private String acaoDividendo;
	@NotNull
	@Positive
	private String valorRecebidoReais;
	@Positive
	private String valorRecebidoAcoes;
	@PastOrPresent
	private String dataDividendo;
	
	public String getAcaoDividendo() {
		return acaoDividendo;
	}
	public void setAcaoDividendo(String acaoDividendo) {
		this.acaoDividendo = acaoDividendo;
	}
	public String getDataDividendo() {
		return dataDividendo;
	}
	public void setDataDividendo(String dataDividendo) {
		this.dataDividendo = dataDividendo;
	}
	public String getValorRecebidoReais() {
		return valorRecebidoReais;
	}
	public void setValorRecebidoReais(String valorRecebidoReais) {
		this.valorRecebidoReais = valorRecebidoReais;
	}
	public String getValorRecebidoAcoes() {
		return valorRecebidoAcoes;
	}
	public void setValorRecebidoAcoes(String valorRecebidoAcoes) {
		this.valorRecebidoAcoes = valorRecebidoAcoes;
	}
	
	
	
}
