package com.jgonite.adapter.http.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.jgonite.domain.dto.DividendoAcoesGanharRequestDTO;
import com.jgonite.domain.dto.ResponseGenericoDTO;
import com.jgonite.domain.service.DividendoAcoesService;

@Controller
@RequestMapping("/api/v1/dividendo-acoes")
public class DividendoAcoesController {
	
	@Autowired private DividendoAcoesService dividendoAcoesService;

	
	@PostMapping("")
	public ResponseEntity<ResponseGenericoDTO> comprarAcao(
			@RequestBody DividendoAcoesGanharRequestDTO requestDTO) {
				try {
					return ResponseEntity.ok(dividendoAcoesService.ganharDividendo(requestDTO));
				} catch (Exception e) {
					ResponseGenericoDTO errorResponse = new ResponseGenericoDTO();
					errorResponse.setMensagem(e.getMessage());
					return ResponseEntity.badRequest().body(errorResponse);
				}
	}
	
	@PostMapping("/uploadDividendosBonificacoes")
	public ResponseEntity<ResponseGenericoDTO> uploadArquivoDividendos(@RequestParam("file") MultipartFile file) {
		if (file.isEmpty()) {
			ResponseGenericoDTO errorResponse = new ResponseGenericoDTO();
			errorResponse.setMensagem("Nao foi adicionado nenhum arquivo para upload!");
			return ResponseEntity.badRequest().body(errorResponse);
		}
		try {
			return ResponseEntity.ok(dividendoAcoesService.fazerUploadArquivo(file));

		} catch (Exception e) {
			ResponseGenericoDTO errorResponse = new ResponseGenericoDTO();
			errorResponse.setMensagem(e.getMessage());
			return ResponseEntity.badRequest().body(errorResponse);
		}
	}
	
}
