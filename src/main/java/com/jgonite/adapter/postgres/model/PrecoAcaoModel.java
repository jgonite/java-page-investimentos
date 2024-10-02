package com.jgonite.adapter.postgres.model;

import java.math.BigDecimal;

import com.jgonite.adapter.postgres.model.compositekey.PrecoAcaoPrimaryKey;

import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity	
@Table(name = "TB_PREC_ACAO")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PrecoAcaoModel {
	
	@EmbeddedId
	private PrecoAcaoPrimaryKey chavePrimaria;
	
	@Column(name = "vl_acao")
    private BigDecimal valorAcao;
	
	@Column(name = "vl_acao_tela")
	private BigDecimal valorAcaoTela;

}
