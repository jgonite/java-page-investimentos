package com.jgonite.domain.process.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.jgonite.port.http.PrecoAcoesPort;
import com.jgonite.port.http.out.YahooPrecoAcoesPort;

@Component
public class YahooPrecosAcoesProcessamentoImpl extends PrecosAcoesProcessamentoImpl {

	@Autowired
	private YahooPrecoAcoesPort port;

	@Override
	public int processar() {
		return super.processar();
	}

	@Override
	protected PrecoAcoesPort getPort() {
		return this.port;
	}
	

}
