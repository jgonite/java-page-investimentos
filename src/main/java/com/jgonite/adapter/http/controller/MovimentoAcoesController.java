package com.jgonite.adapter.http.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.jgonite.domain.dto.MovimentoAcoesRequestDTO;
import com.jgonite.domain.dto.ResponseGenericoDTO;
import com.jgonite.domain.service.MovimentoAcoesService;

@Controller
@RequestMapping("/api/v1/movimento-acoes")
public class MovimentoAcoesController {
	
	@Autowired private MovimentoAcoesService movimentoAcoesService;

	
	@PostMapping("/comprar")
	public ResponseEntity<ResponseGenericoDTO> comprarAcao(
			@RequestBody MovimentoAcoesRequestDTO requestDTO) {
				try {
					return ResponseEntity.ok(movimentoAcoesService.executarCompraVenda(requestDTO));
				} catch (Exception e) {
					ResponseGenericoDTO errorResponse = new ResponseGenericoDTO();
					errorResponse.setMensagem(e.getMessage());
					return ResponseEntity.badRequest().body(errorResponse);
				}
	}

	
	/*
	 * template:
		"DATA_MOVIMENTO;ACAO;NUMERO_ACOEs;VALOR_GASTO
		2024/07/01;LEVE3;50;455,55"
	 */
	@PostMapping("/uploadMovimentacoes")
	public ResponseEntity<ResponseGenericoDTO> uploadArquivoMovimentacoes(@RequestParam("file") MultipartFile file) {
		if (file.isEmpty()) {
			ResponseGenericoDTO errorResponse = new ResponseGenericoDTO();
			errorResponse.setMensagem("Nao foi adicionado nenhum arquivo para upload!");
			return ResponseEntity.badRequest().body(errorResponse);
		}
		try {
			return ResponseEntity.ok(movimentoAcoesService.fazerUploadArquivo(file));

		} catch (Exception e) {
			ResponseGenericoDTO errorResponse = new ResponseGenericoDTO();
			errorResponse.setMensagem(e.getMessage());
			return ResponseEntity.badRequest().body(errorResponse);
		}
	}
	
}
