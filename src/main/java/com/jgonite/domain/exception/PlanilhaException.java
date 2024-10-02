package com.jgonite.domain.exception;

public class PlanilhaException extends RuntimeException {

	private static final long serialVersionUID = 6355556030212146625L;
	
	public PlanilhaException(String message, int linha, int coluna) {
		super("Linha " + String.valueOf(linha) + ", coluna " + String.valueOf(coluna) + ": " + message);
	}

}
