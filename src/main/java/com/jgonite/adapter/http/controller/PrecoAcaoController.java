package com.jgonite.adapter.http.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.jgonite.domain.dto.ResponseGenericoDTO;
import com.jgonite.domain.service.MovimentoAcoesService;
import com.jgonite.domain.service.PrecoAcaoService;

@Controller
@RequestMapping("/api/v1/preco-acao")
public class PrecoAcaoController {
	
	@Autowired private PrecoAcaoService precoAcaoService;

	/*
	 * template:
		"Data","Último","Abertura","Máxima","Mínima","Vol.","Var%"
		"26.08.2024","5,83","5,82","5,83","5,82","3,86M","0,00%"
	 */
	@PostMapping("/uploadPrecos")
	public ResponseEntity<ResponseGenericoDTO> uploadArquivoMovimentacoes(@RequestParam("file") MultipartFile file) {
		if (file.isEmpty()) {
			ResponseGenericoDTO errorResponse = new ResponseGenericoDTO();
			errorResponse.setMensagem("Nao foi adicionado nenhum arquivo para upload!");
			return ResponseEntity.badRequest().body(errorResponse);
		}
		try {
			return ResponseEntity.ok(precoAcaoService.fazerUploadArquivo(file));

		} catch (Exception e) {
			ResponseGenericoDTO errorResponse = new ResponseGenericoDTO();
			errorResponse.setMensagem(e.getMessage());
			return ResponseEntity.badRequest().body(errorResponse);
		}
	}
	
}
