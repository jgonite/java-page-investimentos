package com.jgonite.adapter.postgres.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.jgonite.adapter.postgres.model.PrecoAcaoModel;
import com.jgonite.adapter.postgres.model.compositekey.PrecoAcaoPrimaryKey;

public interface PrecoAcaoRepository extends JpaRepository<PrecoAcaoModel, PrecoAcaoPrimaryKey>  {

}
