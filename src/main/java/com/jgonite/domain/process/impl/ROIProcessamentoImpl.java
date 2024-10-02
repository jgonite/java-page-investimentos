package com.jgonite.domain.process.impl;

import org.springframework.stereotype.Component;

import com.jgonite.domain.process.Processamento;

@Component
public class ROIProcessamentoImpl implements Processamento{

	@Override
	public int processar() {
		return 0;
	}

}
