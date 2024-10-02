package com.jgonite.domain.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Positive;

public class MovimentoAcoesRequestDTO {

	@NotNull
	private String acao;
	@NotNull
	private String compraVenda;
	@NotNull
	@Positive
	private Integer qtdAcoes;
	@NotEmpty
	private String valorBruto;
	@PastOrPresent
	private String dataMovimento;
	
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
	public String getDataMovimento() {
		return dataMovimento;
	}
	public void setDataMovimento(String dataMovimento) {
		this.dataMovimento = dataMovimento;
	}
	public String getCompraVenda() {
		return compraVenda;
	}
	public void setCompraVenda(String compraVenda) {
		this.compraVenda = compraVenda;
	}
	public String getValorBruto() {
		return valorBruto;
	}
	public void setValorBruto(String valorBruto) {
		this.valorBruto = valorBruto;
	}
	
	
}
