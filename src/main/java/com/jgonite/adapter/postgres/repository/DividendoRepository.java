package com.jgonite.adapter.postgres.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.jgonite.adapter.postgres.model.DividendoAcoesModel;
import com.jgonite.adapter.postgres.model.MovimentoAcoesModel;

@Repository
public interface DividendoRepository extends JpaRepository<DividendoAcoesModel, Long> {
		
}
