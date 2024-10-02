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
@Table(name = "tb_stat_ctrl_proc")
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Setter
public class StatusProcessamentoModel {
	
	public static StatusProcessamentoModel INICIADO = StatusProcessamentoModel.builder().idStatus((short)1).descricao("INICIADO").build();
	public static StatusProcessamentoModel PROCESSANDO = StatusProcessamentoModel.builder().idStatus((short)2).descricao("PROCESSANDO").build();
	public static StatusProcessamentoModel FINALIZADO = StatusProcessamentoModel.builder().idStatus((short)3).descricao("FINALIZADO").build();
	public static StatusProcessamentoModel FALHOU = StatusProcessamentoModel.builder().idStatus((short)4).descricao("FALHOU").build();
	
	@Column(name = "NR_IDEN_STAT")
	@Id
	private Short idStatus;
	
	@Column(name = "DS_IDEN_STAT")
	private String descricao;
	
}
