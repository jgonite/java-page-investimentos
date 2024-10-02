package com.jgonite.domain.service;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.jgonite.adapter.postgres.model.InplitSplitModel;
import com.jgonite.adapter.postgres.repository.InplitSplitRepository;
import com.jgonite.domain.dto.ResponseGenericoDTO;
import com.jgonite.domain.exception.PlanilhaException;
import com.jgonite.domain.util.UploadUtils;

@Service
public class InplitSplitService {
	
	@Autowired
	private InplitSplitRepository repository;

	
	@Transactional
	public ResponseGenericoDTO fazerUploadArquivo(MultipartFile file) throws Exception {
		ResponseGenericoDTO response = new ResponseGenericoDTO();
		List<InplitSplitModel> modelList = new ArrayList<>();
		
		try (BufferedReader reader = new BufferedReader(
				new InputStreamReader(file.getInputStream(), StandardCharsets.UTF_8))) {
			int lineNumber = 1;
			String line;
			reader.readLine();
            while ((line = reader.readLine()) != null) {
            	String[] conteudoLinha = line.split(";");
            	modelList.add(UploadUtils.lerLinhaInplitSplit(++lineNumber, conteudoLinha));
            }
		} catch (PlanilhaException e) {
			throw e;
		}
		repository.saveAll(modelList);
		response.setMensagem("Planilha de precos importada com sucesso! " + modelList.size() + " registros adicionados à base!");
		return response;
	}

}
