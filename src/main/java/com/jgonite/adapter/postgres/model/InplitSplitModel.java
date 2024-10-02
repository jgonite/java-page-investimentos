package com.jgonite.adapter.postgres.model;

import java.math.BigDecimal;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity	
@Table(name = "tb_iplt_splt")
@Getter
@NoArgsConstructor
@Setter
public class InplitSplitModel {
		
	@Column(name = "NR_SEQU_IPLT_SPLT")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Id
	private Long idStatus;

	@Column(name = "ODATE")
	private String odate;
	
	@Column(name = "NM_ACAO")
	private String nomeAcao;
	
	@Column(name = "IN_IPLT_SPLT")
	private String tipoInplitSplit;
	
	@Column(name = "VL_IPLT_SPLT")
	private BigDecimal fator;
	
	
}
