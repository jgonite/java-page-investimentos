package com.jgonite.adapter.postgres.model.compositekey;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Embeddable
public class IndicadoresInvesteSiteCompositeKey {

	private String odate;
	private String nmAcao;
	
}
