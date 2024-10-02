package com.jgonite.adapter.postgres.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.jgonite.adapter.postgres.model.compositekey.IndicadoresInvesteSiteCompositeKey;

import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity	
@Table(name = "tb_hist_indi_inve_site")
@Getter
@Setter
@NoArgsConstructor
public class IndicadoresInvesteSiteModel {
	
	@EmbeddedId
	private IndicadoresInvesteSiteCompositeKey chavePrimaria;
	
	@Column(name = "vl_roic")
    private BigDecimal valorRoic;

    @Column(name = "vl_marg_liqu")
    private BigDecimal valorMargemLiquida;

    @Column(name = "vl_marg_ebit")
    private BigDecimal valorMargemEbit;

    @Column(name = "vl_endi")
    private BigDecimal valorEndividamento;

    @Column(name = "dt_ulti_atua")
    private LocalDateTime dataUltimaAtualizacao;
    
	
}
