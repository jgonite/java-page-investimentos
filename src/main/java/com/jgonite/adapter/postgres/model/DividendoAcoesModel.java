package com.jgonite.adapter.postgres.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity	
@Table(name = "tb_divi_boni_acoe")
@Getter
@Setter
@NoArgsConstructor
public class DividendoAcoesModel {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "NR_SEQU_DIVI_ACOE")
	private Long dividendoAcoesId;
	
	@Column(name = "DT_DIVI")
	private LocalDateTime dataDividendo;
	
	@Column(name = "NM_ACAO")
	private String nomeAcao;
	
	@Column(name = "VL_RECE")
	private BigDecimal valorRecebido;
	
	@Column(name = "VL_ACOE_RECE")
	private Integer valorAcoesRecebido;
	
	@Column(name = "VL_ACAO_IR")
	private BigDecimal valorAcaoIr;
	
	@OneToOne
	@JoinColumn(name = "nr_sequ_movi_acoe", referencedColumnName = "nr_sequ_movi_acoe", nullable = false)
	private MovimentoAcoesModel movimentoAcoesModel;	
	
}
