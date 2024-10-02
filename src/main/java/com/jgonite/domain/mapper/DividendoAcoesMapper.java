package com.jgonite.domain.mapper;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import org.springframework.stereotype.Component;

import com.jgonite.adapter.postgres.model.DividendoAcoesModel;
import com.jgonite.adapter.postgres.model.MovimentoAcoesModel;
import com.jgonite.domain.dto.DividendoAcoesDTO;
import com.jgonite.domain.util.StringUtils;

@Component
public class DividendoAcoesMapper {
	
	
	public DividendoAcoesDTO toDto(DividendoAcoesModel model) {
		
		DividendoAcoesDTO dto = new DividendoAcoesDTO();
		dto.setDividendoAcoesId( String.valueOf( model.getDividendoAcoesId()));
		dto.setDataDividendo( StringUtils.localDateTimeParaDDMMYYYYhhmm(model.getDataDividendo()));
		dto.setNomeAcao( model.getNomeAcao() );
		dto.setValorRecebido( StringUtils.bigDecimalParaMoeda(model.getValorRecebido()));
		dto.setValorAcoesRecebido( String.valueOf( model.getValorAcoesRecebido()) );
		return dto;
	}
	
	public MovimentoAcoesModel toMovimento(DividendoAcoesModel dividendo) {
		MovimentoAcoesModel movimento = new MovimentoAcoesModel();
		movimento.setNomeAcao(dividendo.getNomeAcao());
		movimento.setValorBruto(BigDecimal.valueOf(-1).multiply( dividendo.getValorRecebido()));
		movimento.setNumeroAcoes(dividendo.getValorAcoesRecebido());
		movimento.setDataMovimento(dividendo.getDataDividendo());
		movimento.setDataDeCarga(LocalDateTime.now());
		return movimento;
	}

}
