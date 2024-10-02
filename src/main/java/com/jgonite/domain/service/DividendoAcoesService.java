package com.jgonite.domain.service;

import static com.jgonite.domain.util.StringUtils.trocarVirgulaPorPonto;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.jgonite.adapter.postgres.model.DividendoAcoesModel;
import com.jgonite.adapter.postgres.model.MovimentoAcoesModel;
import com.jgonite.adapter.postgres.repository.DividendoRepository;
import com.jgonite.adapter.postgres.repository.MovimentosRepository;
import com.jgonite.domain.dto.DividendoAcoesDTO;
import com.jgonite.domain.dto.DividendoAcoesGanharRequestDTO;
import com.jgonite.domain.dto.ResponseGenericoDTO;
import com.jgonite.domain.exception.PlanilhaException;
import com.jgonite.domain.mapper.DividendoAcoesMapper;
import com.jgonite.domain.util.StringUtils;
import com.jgonite.domain.util.UploadUtils;

@Service
public class DividendoAcoesService {
	
	@Autowired
	private DividendoRepository dividendoRepository;
	@Autowired
	private MovimentosRepository movimentoRepository;
	@Autowired
	private DividendoAcoesMapper mapper;
	
	public ResponseGenericoDTO ganharDividendo(DividendoAcoesGanharRequestDTO requestDTO) {
		
		BigDecimal valorRecebido = new BigDecimal( trocarVirgulaPorPonto( requestDTO.getValorRecebidoReais() )).setScale(2);
		DividendoAcoesModel dividendoModel = new DividendoAcoesModel();
		dividendoModel.setDataDividendo(requestDTO.getDataDividendo() == null ?  LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS) : StringUtils.YYYYMMDDparaLocalDateTime( requestDTO.getDataDividendo()));
		dividendoModel.setNomeAcao(requestDTO.getAcaoDividendo());
		dividendoModel.setValorRecebido(valorRecebido);
		dividendoModel.setValorAcoesRecebido( Integer.parseInt( requestDTO.getValorRecebidoAcoes()) );
		dividendoRepository.save(dividendoModel);
		ResponseGenericoDTO response = new ResponseGenericoDTO();
		response.setMensagem("O dividendo foi lançado com sucesso no banco de dados.");
		return response;
	}
	
	@Transactional
	public ResponseGenericoDTO fazerUploadArquivo(MultipartFile file) throws Exception {
		ResponseGenericoDTO response = new ResponseGenericoDTO();
		long x = 0L;
		try (BufferedReader reader = new BufferedReader(
				new InputStreamReader(file.getInputStream(), StandardCharsets.UTF_8))) {
			int lineNumber = 1;
			String line;
			reader.readLine();
            while ((line = reader.readLine()) != null) {
            	String[] conteudoLinha = line.split(";");
            	DividendoAcoesModel dividendo = UploadUtils.lerLinhaDividendoBonificacao(++lineNumber, conteudoLinha);
            	MovimentoAcoesModel movimento = movimentoRepository.save(mapper.toMovimento(dividendo));
            	dividendo.setMovimentoAcoesModel(movimento);
            	dividendo = dividendoRepository.save(dividendo);
            	movimento.setDividendoAcoesModel(dividendo);
            	movimentoRepository.save(movimento);
            	x++;
            }
		} catch (PlanilhaException e) {
			throw e;
		}
		response.setMensagem("Planilha de dividendos e bonificações importada com sucesso! " + x + " registros adicionados à base!");
		return response;
	}
	
	public List<DividendoAcoesDTO> list() {
		return dividendoRepository.findAll().stream()
				.sorted((o1,o2) -> o2.getDataDividendo().compareTo(o1.getDataDividendo()))
				.map(x->mapper.toDto(x)).collect(Collectors.toList());
	}
	
}
