package com.jgonite.domain.service;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jgonite.adapter.postgres.model.ControleProcessamentoModel;
import com.jgonite.adapter.postgres.model.StatusProcessamentoModel;
import com.jgonite.adapter.postgres.model.TipoProcessamentoModel;
import com.jgonite.adapter.postgres.repository.ControleProcessamentoRepository;
import com.jgonite.domain.process.Processamento;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class ProcessamentoService {

	@Autowired
	private ControleProcessamentoRepository controleProcessamentoRepository;

	@Autowired
	private Map<String, Processamento> processadores;

	public Boolean ultimoProcessamentoEstaEmAndamento(TipoProcessamentoModel tipoProcessamento) {
		Optional<ControleProcessamentoModel> controleProcessamentoOpt = controleProcessamentoRepository
				.obterControleDeProcessamentoDeMaiorId(tipoProcessamento.getCodigoProcessamento());
		return controleProcessamentoOpt.map(c -> c.getStatusProcessamento()).map(s -> s.getIdStatus())
				.map(sp -> sp == StatusProcessamentoModel.INICIADO.getIdStatus() || sp == StatusProcessamentoModel.PROCESSANDO.getIdStatus() ).orElse(false);
	}

	@Transactional
	public ControleProcessamentoModel iniciarNovoProcesso(TipoProcessamentoModel tipoProcessamento) {
		var c = ControleProcessamentoModel.builder().horarioRegistroStatus(LocalDateTime.now())
				.horarioInicio(LocalDateTime.now()).statusProcessamento(StatusProcessamentoModel.INICIADO)
				.tipoProcessamento(tipoProcessamento).build();
		ControleProcessamentoModel novoProcesso = controleProcessamentoRepository.save(c);
		return novoProcesso;
	}

	public void processar(ControleProcessamentoModel processo) {
		try {
			processo.setStatusProcessamento(StatusProcessamentoModel.PROCESSANDO);
			processo.setHorarioRegistroStatus(LocalDateTime.now());
			controleProcessamentoRepository.save(processo);
			int rc = processadores.get(getProcessKey(processo.getTipoProcessamento())).processar();
			if (rc == 0) {
				processo.setStatusProcessamento(StatusProcessamentoModel.FINALIZADO);
			} else {
				processo.setStatusProcessamento(StatusProcessamentoModel.FALHOU);
			}
			processo.setHorarioRegistroStatus(LocalDateTime.now());
			controleProcessamentoRepository.save(processo);
		} catch (Exception e) {
			log.info(String.format(
					"Ocorreu um no processamento, [ID=%s, TIPO=%s]. Os detalhes foram gravados no banco de dados. ",
					processo.getNumeroSequenciaProcessamento(), processo.getTipoProcessamento().getDescricao()));
			processo.setStatusProcessamento(StatusProcessamentoModel.FALHOU);
			processo.setHorarioRegistroStatus(LocalDateTime.now());
			controleProcessamentoRepository.save(processo);
		}
	}

	private String getProcessKey(TipoProcessamentoModel tipoProcessamentoModel) {
		Short codigoTipoProcessamento = tipoProcessamentoModel.getCodigoProcessamento();

		if (codigoTipoProcessamento == TipoProcessamentoModel.ROI.getCodigoProcessamento()) {
			return "ROIProcessamentoImpl";
		} else if (codigoTipoProcessamento == TipoProcessamentoModel.PRECOS_ACOES_BRAPI.getCodigoProcessamento()) {
			return "brapiPrecosAcoesProcessamentoImpl";
		} else if (codigoTipoProcessamento == TipoProcessamentoModel.PRECOS_ACOES_YAHOO.getCodigoProcessamento()) {
			return "yahooPrecosAcoesProcessamentoImpl";
		} else {
			throw new IllegalArgumentException("Tipo de processamento não reconhecido.");
		}
	}

}
