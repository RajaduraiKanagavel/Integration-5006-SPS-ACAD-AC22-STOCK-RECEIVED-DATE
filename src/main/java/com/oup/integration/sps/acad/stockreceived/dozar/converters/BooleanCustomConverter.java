package com.oup.integration.sps.acad.stockreceived.dozar.converters;

public class BooleanCustomConverter {
	
	public boolean stringToBoolean1(String input) {
		if("X".equals(input))
			return true;
		else 
			return false;
	}
	
	public boolean stringToBoolean2(String input) {
		if("Y".equals(input))
			return true;
		else 
			return false;
	}
}
