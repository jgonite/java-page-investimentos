package com.jgonite.adapter.postgres.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.jgonite.adapter.postgres.model.MovimentoAcoesModel;

@Repository
public interface MovimentosRepository extends JpaRepository<MovimentoAcoesModel, Long> {
	
	@Query(value = "SELECT DISTINCT NM_ACAO FROM JINVEST.TB_MOVI_ACOE", nativeQuery=true)
	public List<String> obterListaAcoesRelevantes();
		
}
