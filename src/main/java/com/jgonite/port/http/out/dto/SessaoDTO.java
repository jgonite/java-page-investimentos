package com.jgonite.port.http.out.dto;

import java.util.Map;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
public class SessaoDTO {
	
	private Map<String, String> cookies;
	private String csrfToken;

}
