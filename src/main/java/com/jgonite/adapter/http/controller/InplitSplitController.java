package com.jgonite.adapter.http.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.jgonite.domain.dto.ResponseGenericoDTO;
import com.jgonite.domain.service.InplitSplitService;
import com.jgonite.domain.service.MovimentoAcoesService;
import com.jgonite.domain.service.PrecoAcaoService;

@Controller
@RequestMapping("/api/v1/preco-acao")
public class InplitSplitController {
	
	@Autowired private InplitSplitService service;

	/*
	 * template:
		"ODATE";"ACAO";"TIPO INPLIT SPLIT";"FATOR"
		"20240806";"ALUP4";"I";"10"
	 */
	@PostMapping("/uploadInplitSplit")
	public ResponseEntity<ResponseGenericoDTO> uploadArquivoInpĺitSplit(@RequestParam("file") MultipartFile file) {
		if (file.isEmpty()) {
			ResponseGenericoDTO errorResponse = new ResponseGenericoDTO();
			errorResponse.setMensagem("Nao foi adicionado nenhum arquivo para upload!");
			return ResponseEntity.badRequest().body(errorResponse);
		}
		try {
			return ResponseEntity.ok(service.fazerUploadArquivo(file));

		} catch (Exception e) {
			ResponseGenericoDTO errorResponse = new ResponseGenericoDTO();
			errorResponse.setMensagem(e.getMessage());
			return ResponseEntity.badRequest().body(errorResponse);
		}
	}
	
}
