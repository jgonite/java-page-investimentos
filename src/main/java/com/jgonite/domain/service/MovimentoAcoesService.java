package com.jgonite.domain.service;

import static com.jgonite.domain.util.StringUtils.trocarVirgulaPorPonto;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.jgonite.adapter.postgres.model.MovimentoAcoesModel;
import com.jgonite.adapter.postgres.repository.MovimentosRepository;
import com.jgonite.domain.dto.MovimentoAcoesDTO;
import com.jgonite.domain.dto.MovimentoAcoesRequestDTO;
import com.jgonite.domain.dto.ResponseGenericoDTO;
import com.jgonite.domain.exception.PlanilhaException;
import com.jgonite.domain.mapper.MovimentoAcoesMapper;
import com.jgonite.domain.util.StringUtils;
import com.jgonite.domain.util.UploadUtils;

@Service
public class MovimentoAcoesService {
	
	@Autowired
	private MovimentosRepository movimentoRepository;
	
	@Autowired
	private MovimentoAcoesMapper mapper;
	
	public ResponseGenericoDTO executarCompraVenda(MovimentoAcoesRequestDTO requestDTO) {
		
		String msg1 = "VENDER".equalsIgnoreCase(requestDTO.getCompraVenda()) ? "venda" : "compra";
		String msg2 = "VENDER".equalsIgnoreCase(requestDTO.getCompraVenda()) ? "recuperado" : "investido";
		Integer sinalNumeroAcoes = "VENDER".equalsIgnoreCase(requestDTO.getCompraVenda()) ? -1 : 1;
		
		
		MovimentoAcoesModel movimentoModel = new MovimentoAcoesModel();
		movimentoModel.setDataMovimento(requestDTO.getDataMovimento() == null ?  LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS) : StringUtils.YYYYMMDDparaLocalDateTime(requestDTO.getDataMovimento()));
		movimentoModel.setNomeAcao(requestDTO.getAcao());
		movimentoModel.setNumeroAcoes(sinalNumeroAcoes*requestDTO.getQtdAcoes()); 
		movimentoModel.setValorBruto( StringUtils.stringNumericaParaNumero( requestDTO.getValorBruto()) );
		movimentoRepository.save(movimentoModel);
		
		String valorInvestido = new BigDecimal( trocarVirgulaPorPonto( requestDTO.getValorBruto() )).setScale(2).toPlainString();
		
		ResponseGenericoDTO response = new ResponseGenericoDTO();
		response.setMensagem("A " + msg1 + " foi efetuada com sucesso e lançada no banco de dados."
				+ " O valor total " + msg2 + " foi de R$ ".concat(valorInvestido));
		return response;
	}
	
	@Transactional
	public ResponseGenericoDTO fazerUploadArquivo(MultipartFile file) throws Exception {
		ResponseGenericoDTO response = new ResponseGenericoDTO();
		List<MovimentoAcoesModel> modelList = new ArrayList<>();
		try (BufferedReader reader = new BufferedReader(
				new InputStreamReader(file.getInputStream(), StandardCharsets.UTF_8))) {
			int lineNumber = 1;
			String line;
			reader.readLine();
            while ((line = reader.readLine()) != null) {
            	String[] conteudoLinha = line.split(";");
            	modelList.add(UploadUtils.lerLinhaMovimento(++lineNumber, conteudoLinha));
            }
		} catch (PlanilhaException e) {
			throw e;
		}
		movimentoRepository.saveAll(modelList);
		response.setMensagem("Planilha de movimentos importada com sucesso! " + modelList.size() + " registros adicionados à base!");
		return response;
	}
	
	public List<MovimentoAcoesDTO> list() {
		return movimentoRepository.findAll().stream().sorted(
				(o1,o2) -> o2.getDataMovimento().compareTo(o1.getDataMovimento())
				).map(x->mapper.toDto(x)).collect(Collectors.toList());
	}

}
