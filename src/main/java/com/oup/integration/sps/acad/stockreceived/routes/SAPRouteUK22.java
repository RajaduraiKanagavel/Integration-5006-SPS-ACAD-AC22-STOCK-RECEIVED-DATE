package com.oup.integration.sps.acad.stockreceived.routes;

import org.apache.camel.builder.RouteBuilder;
import org.springframework.stereotype.Component;

@Component("sapRouteUK22")
public class SAPRouteUK22 extends RouteBuilder {

	@Override
	public void configure() throws Exception {
		
		from("ftp:{{ftp.uk.server}}:{{ftp.uk.port}}{{ftp.uk.drop.location}}?include=ac22.*.dat&doneFileName=${file:name.noext}.go&move=#springManagedMMFileRename&password={{ftp.uk.password}}&username={{ftp.uk.username}}&passiveMode=true&disconnect=true")
			.routeId("sapRouteUK")
			.convertBodyTo(String.class)
			.setHeader("RequestDate", simple("${date:now:ddMMyyyy}"))
			.setHeader("RequestTime", simple("${date:now:HHmmss}"))
			.setHeader("RequestReceivedTime", simple("${date:now:HHmmssSSS}"))
			.setHeader("InterfaceName", simple("ac22"))
			.setHeader("Region", simple("UK"))
			.setHeader("InterfaceFullName", simple("ACAD AC22 Stock Received Date"))
			.to("direct:ReceivedSAPMessageAC22");
		
	}

}
