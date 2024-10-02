package com.jgonite.domain.process.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import org.springframework.beans.factory.annotation.Autowired;

import com.jgonite.adapter.postgres.model.PrecoAcaoModel;
import com.jgonite.adapter.postgres.repository.MovimentosRepository;
import com.jgonite.adapter.postgres.repository.PrecoAcaoRepository;
import com.jgonite.domain.process.Processamento;
import com.jgonite.port.http.PrecoAcoesPort;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public abstract class PrecosAcoesProcessamentoImpl implements Processamento {

	@Autowired
	private MovimentosRepository movimentosRepository;

	@Autowired
	private PrecoAcaoRepository precoAcaoRepository;

	@Override
	public int processar() {
		int rc = 0;
		List<String> acoes = movimentosRepository.obterListaAcoesRelevantes();
		List<Callable<List<PrecoAcaoModel>>> listaDeChamadas = new ArrayList<>();
		
		for (String acao : acoes) {
			Callable<List<PrecoAcaoModel>> chamada = () -> {
				return this.getPort().obterListPrecoAcaoModel(acao);
			};
			listaDeChamadas.add(chamada);
		}
		
		ExecutorService executor = Executors.newFixedThreadPool(acoes.size() / 2);
		List<Future<List<PrecoAcaoModel>>> execucoes = new ArrayList<>(acoes.size());
		List<List<PrecoAcaoModel>> listaDeResultados = new ArrayList<>(acoes.size());
		try {
			execucoes = executor.invokeAll(listaDeChamadas);
			for (Future<List<PrecoAcaoModel>> execucao : execucoes) {
				if (!execucao.isCancelled()) {
					try {
						listaDeResultados.add(execucao.get());
					} catch (ExecutionException e) {
						log.error("Execução de chamada de preço ações falhou: " + e.getMessage());
					}
				}
			}
		} catch (InterruptedException ie) {
			log.error("Execuções de chamadas de preço-ações foi interrompida: " + ie.getMessage());
			rc = 1;
		}

		for (List<PrecoAcaoModel> resultados : listaDeResultados) {
			int counterResultado = 0;
			String nomeResultado = "";
			String oDateResultado = "";
			for (PrecoAcaoModel resultado : resultados) {
				if ("".equalsIgnoreCase(nomeResultado) && "".equalsIgnoreCase(oDateResultado)) {
					nomeResultado = resultado.getChavePrimaria().getNmAcao();
					oDateResultado = resultado.getChavePrimaria().getOdate();
				}
				try {
					resultado.setValorAcaoTela(resultado.getValorAcao());
					var obj = precoAcaoRepository.save(resultado);
					if (obj != null) {
						counterResultado++;
					}
				} catch (Exception e) {
					log.error("Falhou ao gravar preço: " + resultado.getChavePrimaria().getOdate() + " => "
							+ resultado.getChavePrimaria().getNmAcao() + ": " + e.getMessage());
				}
			}
			log.info("Gravados " + counterResultado + " registros para: " + oDateResultado + " => " + nomeResultado);
		}
		return rc;
	}
	
	protected abstract PrecoAcoesPort getPort();

}
