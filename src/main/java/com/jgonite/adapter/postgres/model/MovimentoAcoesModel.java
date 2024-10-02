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
@Table(name = "TB_MOVI_ACOE")
@Getter
@Setter
@NoArgsConstructor
public class MovimentoAcoesModel {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "NR_SEQU_MOVI_ACOE")
	private Long movimentoAcoesId;
	
	@Column(name = "DT_MOVI")
	private LocalDateTime dataMovimento;
	
	@Column(name = "NM_ACAO")
	private String nomeAcao;
	
	@Column(name = "NR_ACOE")
	private Integer numeroAcoes;
	
	@Column(name = "VL_BRUT")
	private BigDecimal valorBruto;
	
	@Column(name = "DT_CARG")
	private LocalDateTime dataDeCarga;
	
	@OneToOne
	@JoinColumn(name = "nr_sequ_divi_acoe", referencedColumnName = "nr_sequ_divi_acoe", nullable = true)
	private DividendoAcoesModel dividendoAcoesModel;	


}
