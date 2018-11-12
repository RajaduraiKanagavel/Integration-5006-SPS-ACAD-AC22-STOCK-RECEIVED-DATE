package com.oup.integration.sps.acad.stockreceived.dozar.converters;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;

public class DateFormatCustomConverter {
	
	public String converterFormat1(String input) throws ParseException {
		DateFormat from = new SimpleDateFormat("yyyyMMdd");
		DateFormat to = new SimpleDateFormat("ddMMyyyy"); 
		return to.format(from.parse(input));
	}
}
