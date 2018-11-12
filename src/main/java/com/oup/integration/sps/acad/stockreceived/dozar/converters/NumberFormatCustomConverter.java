package com.oup.integration.sps.acad.stockreceived.dozar.converters;

import org.apache.commons.lang3.StringUtils;

public class NumberFormatCustomConverter {
	
	public Double stringToDouble(String input) {
		if( StringUtils.isBlank(input) ) {
			return 0D;
		}else if(StringUtils.endsWith(input, "-")) {
			return Double.parseDouble("-"+input.substring(0, input.length()-1));
		}else {
			return Double.parseDouble(input);
		}
	}
	

}
