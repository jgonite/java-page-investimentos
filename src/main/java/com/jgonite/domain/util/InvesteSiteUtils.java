package com.jgonite.domain.util;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class InvesteSiteUtils {
	
	private static String TEMPLATE = "YYYYMMDD";
	
	public static String substituirOdateNaUrl(String url) {
		LocalDate date = LocalDate.now().minusDays(2);
        DayOfWeek dayOfWeek = date.getDayOfWeek();
        if (dayOfWeek == DayOfWeek.SUNDAY) {
            date = date.minusDays(2);
        } else if (dayOfWeek == DayOfWeek.SATURDAY) {
            date = date.minusDays(1); 
        }
        String odate = date.format(DateTimeFormatter.ofPattern("yyyyMMdd"));

        return url.replaceAll(TEMPLATE, odate).intern();
	}

}
