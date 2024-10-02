package com.jgonite.adapter.http.controller;

import java.util.concurrent.CompletableFuture;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.jgonite.adapter.postgres.model.ControleProcessamentoModel;
import com.jgonite.adapter.postgres.model.TipoProcessamentoModel;
import com.jgonite.domain.dto.ResponseGenericoDTO;
import com.jgonite.domain.service.ProcessamentoService;

@Controller
@RequestMapping("/api/v1/webhooks")
public class WebHooksController {

	@Autowired
	private ProcessamentoService processamentoRentabilidadeService;

	/**
     * Atualiza o banco de dados Postgresql com os dados de preços dos últimos 3 meses 
     * de todas as ações relevantes para a rentabilidade da carteira 
     * Para ser utilizada no dia-a-dia. Traz dados de API online gratuíta do Brapi.
     * @return mensagem com resultado da requisiçãod e processamento.
     */
	@PostMapping("/processar-precos-acoes")
	public ResponseEntity<ResponseGenericoDTO> processarPrecosAcoes(
			@RequestParam("yahooScrapping") Boolean fazerScrappingDaYahooFinance) {
			return fazerScrappingDaYahooFinance ? rodarProcesso(TipoProcessamentoModel.PRECOS_ACOES_YAHOO) : rodarProcesso(TipoProcessamentoModel.PRECOS_ACOES_BRAPI);
	}
	
	@PostMapping("/processar-roi")
	public ResponseEntity<ResponseGenericoDTO> processarRentabilidade() {
		return rodarProcesso(TipoProcessamentoModel.ROI);
	}
	
	private ResponseEntity<ResponseGenericoDTO> rodarProcesso(TipoProcessamentoModel tipoProcessamento) {
		ResponseGenericoDTO response = new ResponseGenericoDTO();
		try {
			if (processamentoRentabilidadeService.ultimoProcessamentoEstaEmAndamento(tipoProcessamento)) {
				response.setMensagem(
						"Há um processamento de cálculo de Rentabilidade em andamento, necessário aguardar o teŕmino!");
				return ResponseEntity.unprocessableEntity().body(response);
			} else {
				ControleProcessamentoModel processo = processamentoRentabilidadeService
						.iniciarNovoProcesso(tipoProcessamento);
				CompletableFuture.runAsync(() -> processamentoRentabilidadeService.processar(processo));
				response.setMensagem("Processamento requisitado com sucesso!");
				return ResponseEntity.ok(response);
			}
		} catch (Exception e) {
			response.setMensagem("Houve um erro ao processar pedido: " + e.getMessage());
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
		}
	}

}
