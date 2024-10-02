package com.jgonite.port.http.out.dto;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BrapiRootResponseDTO {

	public List<BrapiResultResponseDTO> results;
}
