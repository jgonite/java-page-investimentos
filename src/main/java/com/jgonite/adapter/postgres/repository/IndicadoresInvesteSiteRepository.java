package com.jgonite.adapter.postgres.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.jgonite.adapter.postgres.model.IndicadoresInvesteSiteModel;
import com.jgonite.adapter.postgres.model.compositekey.IndicadoresInvesteSiteCompositeKey;

@Repository
public interface IndicadoresInvesteSiteRepository extends JpaRepository<IndicadoresInvesteSiteModel, IndicadoresInvesteSiteCompositeKey> {

	
	
}
