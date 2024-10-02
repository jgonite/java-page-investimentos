package com.jgonite.domain.entity;

import java.math.BigDecimal;

public class CarteiraAcaoEntity {
	
	private String acao = "";
	private Integer qtdAcoes = 0;
	private BigDecimal valorInvestidoDesinvestido = BigDecimal.ZERO;
	private BigDecimal valorAtual = BigDecimal.ZERO;
	private BigDecimal valorProventos = BigDecimal.ZERO;
	private BigDecimal precoMedio = BigDecimal.ZERO;
	private BigDecimal ROI = BigDecimal.ZERO;
	private BigDecimal percCarteira = BigDecimal.ZERO;
	
	public String getAcao() {
		return acao;
	}
	public void setAcao(String acao) {
		this.acao = acao;
	}
	public Integer getQtdAcoes() {
		return qtdAcoes;
	}
	public void setQtdAcoes(Integer qtdAcoes) {
		this.qtdAcoes = qtdAcoes;
	}
	public void incrementQtdAcoes(Integer augend) {
		this.qtdAcoes = this.qtdAcoes + augend;
	}
	public BigDecimal getValorInvestidoDesinvestido() {
		return valorInvestidoDesinvestido;
	}
	public void setValorInvestidoDesinvestido(BigDecimal valorInvestido) {
		this.valorInvestidoDesinvestido = valorInvestido;
	}
	public void incrementValorInvestidoDesinvestido(BigDecimal augend) {
		this.valorInvestidoDesinvestido = this.valorInvestidoDesinvestido.add(augend);
	}
	
	public BigDecimal getValorAtual() {
		return valorAtual;
	}
	public void setValorAtual(BigDecimal valorAtual) {
		this.valorAtual = valorAtual;
	}
	public BigDecimal getValorProventos() {
		return valorProventos;
	}
	public void setValorProventos(BigDecimal valorProventos) {
		this.valorProventos = valorProventos;
	}
	public BigDecimal getPrecoMedio() {
		return precoMedio;
	}
	public void setPrecoMedio(BigDecimal precoMedio) {
		this.precoMedio = precoMedio;
	}
	public BigDecimal getROI() {
		return ROI;
	}
	public void setROI(BigDecimal rOI) {
		ROI = rOI;
	}
	public BigDecimal getPercCarteira() {
		return percCarteira;
	}
	public void setPercCarteira(BigDecimal percCarteira) {
		this.percCarteira = percCarteira;
	}
	

}
