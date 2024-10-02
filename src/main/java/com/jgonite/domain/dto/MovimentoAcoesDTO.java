package com.jgonite.domain.dto;

public class MovimentoAcoesDTO {
	
	private String movimentoAcoesId;
	private String dataMovimento;
	private String nomeAcao;
	private String numeroAcoes;
	private String valorTransacao;
	
	public String getMovimentoAcoesId() {
		return movimentoAcoesId;
	}
	public void setMovimentoAcoesId(String movimentoAcoesId) {
		this.movimentoAcoesId = movimentoAcoesId;
	}
	public String getDataMovimento() {
		return dataMovimento;
	}
	public void setDataMovimento(String dataMovimento) {
		this.dataMovimento = dataMovimento;
	}
	public String getNomeAcao() {
		return nomeAcao;
	}
	public void setNomeAcao(String nomeAcao) {
		this.nomeAcao = nomeAcao;
	}
	public String getNumeroAcoes() {
		return numeroAcoes;
	}
	public void setNumeroAcoes(String numeroAcoes) {
		this.numeroAcoes = numeroAcoes;
	}

	public String getValorTransacao() {
		return valorTransacao;
	}
	public void setValorTransacao(String valorTransacao) {
		this.valorTransacao = valorTransacao;
	}
	
	
	
}
