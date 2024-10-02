package com.jgonite.domain.process.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.jgonite.port.http.PrecoAcoesPort;
import com.jgonite.port.http.out.BrapiPrecoAcoesPort;

@Component
public class BrapiPrecosAcoesProcessamentoImpl extends PrecosAcoesProcessamentoImpl {

	@Autowired
	private BrapiPrecoAcoesPort port;

	@Override
	public int processar() {
		return super.processar();
	}

	@Override
	protected PrecoAcoesPort getPort() {
		return this.port;
	}
	

}
