package com.jgonite.port.http.out.dto;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BrapiResultResponseDTO {
	
    public String symbol;
    public String usedInterval;
    public String usedRange;
    public List<BrapiHistoricalDataPrice> historicalDataPrice;

}
