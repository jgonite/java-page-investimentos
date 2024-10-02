package com.jgonite.port.http.out.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BrapiHistoricalDataPrice {
	
	public long date;
    public double open;
    public double high;
    public double low;
    public double close;
    public int volume;
    public double adjustedClose;

}
