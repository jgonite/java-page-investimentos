package com.jgonite.adapter.postgres.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity	
@Table(name = "tb_tipo_proc")
@AllArgsConstructor
@Builder
@Getter
@NoArgsConstructor
@Setter
public class TipoProcessamentoModel {
	
	public static TipoProcessamentoModel ROI = TipoProcessamentoModel.builder().codigoProcessamento((short)1).descricao("ROI").build();
	public static TipoProcessamentoModel PRECOS_ACOES_BRAPI = TipoProcessamentoModel.builder().codigoProcessamento((short)2).descricao("PRECO_ACOES_BRAPI").build();
	public static TipoProcessamentoModel PRECOS_ACOES_YAHOO = TipoProcessamentoModel.builder().codigoProcessamento((short)3).descricao("PRECO_ACOES_YAHOO").build();
	
	@Column(name = "cd_tipo_proc")
	@Id
	private Short codigoProcessamento;
	
	@Column(name = "ds_tipo_proc")
	private String descricao;	

}
