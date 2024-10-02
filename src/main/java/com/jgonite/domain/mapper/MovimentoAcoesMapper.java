package com.jgonite.domain.mapper;

import org.springframework.stereotype.Component;

import com.jgonite.adapter.postgres.model.MovimentoAcoesModel;
import com.jgonite.domain.dto.MovimentoAcoesDTO;
import com.jgonite.domain.util.StringUtils;

@Component
public class MovimentoAcoesMapper {
	
	
	public MovimentoAcoesDTO toDto(MovimentoAcoesModel model) {
		
		MovimentoAcoesDTO dto = new MovimentoAcoesDTO();
		dto.setDataMovimento( StringUtils.localDateTimeParaDDMMYYYYhhmm(model.getDataMovimento()));
		dto.setMovimentoAcoesId( String.valueOf( model.getMovimentoAcoesId() ));
		dto.setNomeAcao(model.getNomeAcao());
		dto.setNumeroAcoes( String.valueOf( model.getNumeroAcoes() ));
		dto.setValorTransacao( StringUtils.bigDecimalParaMoeda( model.getValorBruto() ));
		
		return dto;
	}

}
