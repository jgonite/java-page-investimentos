package com.jgonite.domain.dto;

import java.util.List;

public class CarteiraAcoesDTO {
	
	private String valorInvestido;
	private String valorAtual;
	private String valorProventos;
	private String ROI;
	private List<CarteiraAcaoDTO> carteiraAcoes;
	
	
	public String getValorInvestido() {
		return valorInvestido;
	}
	public void setValorInvestido(String valorInvestido) {
		this.valorInvestido = valorInvestido;
	}
	public String getValorAtual() {
		return valorAtual;
	}
	public void setValorAtual(String valorAtual) {
		this.valorAtual = valorAtual;
	}
	public String getValorProventos() {
		return valorProventos;
	}
	public void setValorProventos(String valorProventos) {
		this.valorProventos = valorProventos;
	}
	public String getROI() {
		return ROI;
	}
	public void setROI(String rOI) {
		ROI = rOI;
	}
	public List<CarteiraAcaoDTO> getCarteiraAcoes() {
		return carteiraAcoes;
	}
	public void setCarteiraAcoes(List<CarteiraAcaoDTO> carteiraAcoes) {
		this.carteiraAcoes = carteiraAcoes;
	}
	
	

}
