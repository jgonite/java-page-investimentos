package com.jgonite.domain.util;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class MathUtils {
	
	public static BigDecimal valorStringComoNumerico(String s) {
		return new BigDecimal(s.replaceAll("\\.", "").replaceAll(",", "."));
	}
	
	public static BigDecimal valorPorcentagemComoNumerico(String s) {
		return new BigDecimal(s.replaceAll("\\.", "").replaceAll(",", ".").replaceAll("%", ""))
				.divide(new BigDecimal("100"), RoundingMode.HALF_UP);
	}

}
