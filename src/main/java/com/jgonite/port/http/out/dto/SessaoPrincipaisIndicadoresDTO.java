package com.jgonite.port.http.out.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class SessaoPrincipaisIndicadoresDTO extends SessaoDTO {
	
	private String tipoemp;
	private String codcvm;
	private String outerHml;

}
