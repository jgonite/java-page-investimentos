package com.jgonite.port.http;

import java.util.List;

import com.jgonite.adapter.postgres.model.PrecoAcaoModel;

public interface PrecoAcoesPort {
	
	List<PrecoAcaoModel> obterListPrecoAcaoModel(String ticker);

}
