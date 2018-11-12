package com.oup.integration.sps.acad.stockreceived.routes;

import org.apache.camel.builder.RouteBuilder;
import org.springframework.stereotype.Component;

@Component("sapRouteUS22")
public class SAPRouteUS22 extends RouteBuilder {

	@Override
	public void configure() throws Exception {
		
		from("ftp:{{ftp.us.server}}:{{ftp.us.port}}{{ftp.us.drop.location}}?include=us22.*.dat&doneFileName=${file:name.noext}.go&move=#springManagedMMFileRename&password={{ftp.us.password}}&username={{ftp.us.username}}&passiveMode=true&disconnect=true")
			
		/*from("file:C:\\input")*/
		.routeId("sapRouteUS22")
			.convertBodyTo(String.class)
			.setHeader("RequestDate", simple("${date:now:ddMMyyyy}"))
			.setHeader("RequestTime", simple("${date:now:HHmmss}"))
			.setHeader("RequestReceivedTime", simple("${date:now:HHmmssSSS}"))
			.setHeader("InterfaceName", simple("us22"))
			.setHeader("Region", simple("US"))
			.setHeader("InterfaceFullName", simple("ACAD US22 Stock Received Date"))
			.to("direct:ReceivedSAPMessageUS22");
		
	}

}
