package com.oup.integration.sps.acad.stockreceived.routes;

import org.apache.camel.LoggingLevel;
import org.apache.camel.builder.RouteBuilder;
import org.dozer.MappingException;
import org.springframework.stereotype.Component;


@Component
public class RouteToGenerateIndividualSAPUS22 extends RouteBuilder {

	@Override
	public void configure() throws Exception {

		onException(MappingException.class, IllegalStateException.class, NullPointerException.class,
				NumberFormatException.class).maximumRedeliveries(0).log(LoggingLevel.ERROR, "com.oup.sps",
						"Exceptions due to data issues in the file processing in ${routeId} .\n Exception Message: ${exchangeProperty.CamelExceptionCaught}")
						.setBody(simple(
								"Exceptions due to data issues in the file processing in ${routeId} .\n Exception Message: ${exchangeProperty.CamelExceptionCaught}"))
						.to("file:{{file.backup.location}}/Error?fileName=${date:now:yyyy/MM/dd/}$simple{header.DestinationFileName}_$simple{header.RequestReceivedTime}.json")
						.handled(true);

		onException(Exception.class).maximumRedeliveries(0).log(LoggingLevel.ERROR, "com.oup.sps",
				"Exception occurred while processing messages in ${routeId} Exception Message: ${exchangeProperty.CamelExceptionCaught}")
				.setBody(simple(
						"Exception occurred while processing messages in ${routeId} Exception Message: ${exchangeProperty.CamelExceptionCaught}"))
				.to("file:{{file.backup.location}}/Error?fileName=${date:now:yyyy/MM/dd/}$simple{header.DestinationFileName}_$simple{header.RequestReceivedTime}.json")
				.handled(true);

		from("direct:transform_sap_to_biblioUS22").routeId("routeToGenerateIndividualSAPUS22")
				

		.to("dozer:SAP_To_Biblio_Transformationus?sourceModel=com.oup.integration.sps.acad.stockreceived.sap.pojo.StockreceiveddateUS22&targetModel=com.oup.integration.sps.acad.stockreceived.biblio.pojo.StockreceiveddateBiblioUS22&mappingFile=Transformations/SAP_To_Biblio_TransformationUS22.xml");

	}

}
