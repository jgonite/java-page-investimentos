package com.jgonite.adapter.postgres.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.jgonite.adapter.postgres.model.ControleProcessamentoModel;

@Repository
public interface ControleProcessamentoRepository extends JpaRepository<ControleProcessamentoModel, Long> {

	@Query(value = "SELECT * FROM JINVEST.TB_CTRL_PROC CONTROLE "
			+ " WHERE CONTROLE.NR_SEQU_PROC = "
			+ " (SELECT MAX(NR_SEQU_PROC) FROM JINVEST.TB_CTRL_PROC) AND CONTROLE.CD_TIPO_PROC = :codigoTipoProcessamento", 
			nativeQuery = true)
    public Optional<ControleProcessamentoModel> obterControleDeProcessamentoDeMaiorId(
    			@Param("codigoTipoProcessamento") Short codigoTipoProcessamento
    		);
		
}
