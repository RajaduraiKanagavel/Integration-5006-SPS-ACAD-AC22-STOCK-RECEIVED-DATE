package com.oup.integration.sps.acad.stockreceived.routes;

import java.io.IOException;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.FileSystemException;

import org.apache.camel.LoggingLevel;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.file.GenericFileOperationFailedException;
import org.apache.camel.dataformat.bindy.csv.BindyCsvDataFormat;
import org.apache.camel.model.dataformat.JsonLibrary;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import com.google.gson.JsonSyntaxException;
import com.oup.integration.sps.acad.stockreceived.processor.SAPFileProcessor;
import com.oup.integration.sps.acad.stockreceived.sap.pojo.StockreceiveddateUS22;


@Component
public class MainRouteUS22 extends RouteBuilder {

	@Override
	public void configure() throws Exception {
		
		onException(JsonSyntaxException.class, InvalidFormatException.class, IllegalStateException.class, NullPointerException.class, IllegalArgumentException.class, NumberFormatException.class, StringIndexOutOfBoundsException.class)
			.maximumRedeliveries(0)
			.log(LoggingLevel.ERROR, "com.oup.sps", "Exceptions due to data issues in the ${header.InterfaceFullName} message processing in Route ${routeId} .\n Exception Message: ${exchangeProperty.CamelExceptionCaught}")
			.setBody(simple("Exceptions due to data issues in the ${header.InterfaceFullName} message processing in Route ${routeId} .\\n Exception Message: ${exchangeProperty.CamelExceptionCaught}"))
			.to("file:{{file.backup.location}}/Error?fileName=${date:now:yyyy/MM/dd/}$simple{property.ReceivedFileName}_$simple{header.RequestReceivedTime}.txt")
			.handled(true);
			
		onException(IOException.class, GenericFileOperationFailedException.class, FileSystemException.class, FileAlreadyExistsException.class)
			.maximumRedeliveries(3)
			.maximumRedeliveryDelay(300000)
			.log(LoggingLevel.ERROR, "com.oup.sps", "Exception due to IO operations in ${header.InterfaceFullName} message processing in Route ${routeId} .\\n Exception Message: ${exchangeProperty.CamelExceptionCaught}")
			.setBody(simple("Exception due to IO operations in ${header.InterfaceFullName} message processing in Route ${routeId} .\\n Exception Message: ${exchangeProperty.CamelExceptionCaught}"))
			.to("file:{{file.backup.location}}/Error?fileName=${date:now:yyyy/MM/dd/}$simple{property.ReceivedFileName}_$simple{header.RequestReceivedTime}.txt")
			.handled(true);
		
		onException(Exception.class)
			.maximumRedeliveries(0)
			.log(LoggingLevel.ERROR, "com.oup.sps", "Exception occurred in ${header.InterfaceFullName} message processing in Route ${routeId} .\\n Exception Message: ${exchangeProperty.CamelExceptionCaught}")
			.setBody(simple("Exception occurred in ${header.InterfaceFullName} message processing in Route ${routeId}.\\n Exception Message: ${exchangeProperty.CamelExceptionCaught}"))
			.to("file:{{file.backup.location}}/Error?fileName=${date:now:yyyy/MM/dd/}$simple{property.ReceivedFileName}_$simple{header.RequestReceivedTime}.txt")
			.handled(true);
		
		from("direct:ReceivedSAPMessageUS22")
			.routeId("mainRouteUS22")
			.convertBodyTo(String.class)
			.wireTap("file:{{file.backup.location}}/1.0 ReceivedFromSAP?fileName=${date:now:yyyy/MM/dd}$simple{header.CamelFileName}_$simple{header.RequestReceivedTime}.dat")
			.log(LoggingLevel.INFO, "com.oup.sps", "Received SAP MM file ${header.CamelFileName} Message ${body}")
			.process(new SAPFileProcessor())
			
			
			.unmarshal(new BindyCsvDataFormat(StockreceiveddateUS22.class))
			
			
			.log(LoggingLevel.INFO, "com.oup.sps", "Split Started for MM file ${header.CamelFileName}")
			.split(body())
				.streaming()
				.parallelProcessing()
				.to("direct:transform_sap_to_biblioUS22")
				.setHeader("productISBN", jsonpath("$.productISBN").regexReplaceAll(":", "-"))
				
				.marshal().json(JsonLibrary.Jackson)
				
				.log(LoggingLevel.INFO, "com.oup.sps", "Constructed Biblio message for ISBN ${header.productISBN} : ${body}")
				.wireTap("file:{{file.backup.location}}/2.0 SentToBiblio?fileName=${date:now:yyyy/MM/dd/}$simple{header.CamelFileName}_$simple{header.productISBN}.json")
				.to("activemq:{{activemq.us.queuename}}")
				.log(LoggingLevel.INFO, "com.oup.sps", "${header.InterfaceFullName} file ${header.productISBN} generated");
			
	}

}
