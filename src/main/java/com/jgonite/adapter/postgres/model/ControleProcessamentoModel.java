package com.jgonite.adapter.postgres.model;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.PrimaryKeyJoinColumn;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity	
@Table(name = "tb_ctrl_proc")
@AllArgsConstructor
@Builder
@Getter
@NoArgsConstructor
@Setter
public class ControleProcessamentoModel {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "NR_SEQU_PROC")
	private Long numeroSequenciaProcessamento;
	
	@ManyToOne
	@JoinColumn(name = "CD_TIPO_PROC", referencedColumnName = "CD_TIPO_PROC")
	private TipoProcessamentoModel tipoProcessamento;
	
	@ManyToOne
	@JoinColumn(name = "NR_IDEN_STAT", referencedColumnName = "NR_IDEN_STAT")
	private StatusProcessamentoModel statusProcessamento;
	
	@Column(name = "DT_IDEN_STAT")
	private LocalDateTime horarioRegistroStatus;
	
	@Column(name = "DT_STAT_INIC")
	private LocalDateTime horarioInicio;
	
}
