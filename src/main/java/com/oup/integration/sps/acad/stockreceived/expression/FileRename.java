package com.oup.integration.sps.acad.stockreceived.expression;

import org.apache.camel.Exchange;
import org.apache.camel.Expression;
import org.springframework.stereotype.Component;

@Component("springManagedMMFileRename")
public class FileRename implements Expression  {

	@Override
	public <String> String evaluate(Exchange arg0, Class<String> arg1) {
		java.lang.String fileName= arg0.getIn().getHeader("CamelFileName").toString();		
		java.lang.String newFileName="x"+fileName.substring(1, fileName.length());		
		return (String) newFileName;
	}

}