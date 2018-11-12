package com.oup.integration.sps.acad.stockreceived;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import org.apache.camel.CamelContext;
import org.apache.camel.EndpointInject;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.TypeConverter;
import org.apache.camel.builder.AdviceWithRouteBuilder;
import org.apache.camel.component.mock.MockEndpoint;
import org.apache.camel.model.RouteDefinition;
import org.apache.camel.test.spring.CamelSpringBootRunner;
import org.apache.camel.test.spring.MockEndpoints;
import org.apache.camel.test.spring.MockEndpointsAndSkip;
import org.apache.camel.test.spring.UseAdviceWith;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;

@RunWith(CamelSpringBootRunner.class)
@SpringBootTest
@DirtiesContext(classMode = ClassMode.AFTER_EACH_TEST_METHOD) //ensure that the CamelContext, routes, and mock endpoints are reinitialized between test methods.
@MockEndpointsAndSkip("ftp:.*|file:.*|activemq:.*") //All endpoints are sniffed and recorded in a mock endpoint. The original endpoint is not invoked.
@MockEndpoints // All endpoints are sniffed and recorded in a mock endpoint.
@UseAdviceWith
public class TitleMasterDataApplicationTests {
	
	@Autowired
	private CamelContext camelContext;
	
	@EndpointInject(uri = "direct:input")
    protected ProducerTemplate ftpserver;
	
	@EndpointInject(uri="mock:activemq:{{activemq.uk.queuename}}")
	private MockEndpoint dropLocationActiveMQ;
	
	@EndpointInject(uri="mock:file:{{file.backup.location}}/Error")
	private MockEndpoint backupErrorLocation;
	
	@EndpointInject(uri="mock:file:{{file.backup.location}}/1.0 ReceivedFromSAP")
	private MockEndpoint backupSAPLocation;
	
	@EndpointInject(uri="mock:file:{{file.backup.location}}/2.0 SentToBiblio")
	private MockEndpoint backupBiblioLocation;
	
	@Before
	public void setUp() throws Exception {
		RouteDefinition rf =  camelContext.getRouteDefinition("sapRouteUK");
		rf.adviceWith(camelContext, new AdviceWithRouteBuilder() {

			@Override
			public void configure() throws Exception {
				replaceFromWith("direct:input");
			}
			
		});
		camelContext.start();
	}

	@Test
	public void validSAP() throws InterruptedException {
		TypeConverter typeConverter = camelContext.getTypeConverter();
		
		dropLocationActiveMQ.expectedMessageCount(7);
		backupSAPLocation.expectedMessageCount(1);
		backupBiblioLocation.expectedMessageCount(7);
		
		String body = typeConverter.convertTo(String.class, new File("src/test/resources/ac160001.dat"));
		backupSAPLocation.expectedBodiesReceived(body);
		
		List<String> expectedBodies = new ArrayList<>();
		expectedBodies.add(typeConverter.convertTo(String.class, new File("src/test/resources/ac160001.dat_COMLIV-C.json")));
		expectedBodies.add(typeConverter.convertTo(String.class, new File("src/test/resources/ac160001.dat_9780192574794.json")));
		expectedBodies.add(typeConverter.convertTo(String.class, new File("src/test/resources/ac160001.dat_9780192574855.json")));
		expectedBodies.add(typeConverter.convertTo(String.class, new File("src/test/resources/ac160001.dat_9780198838548.json")));
		expectedBodies.add(typeConverter.convertTo(String.class, new File("src/test/resources/ac160001.dat_9780191874758.json")));
		expectedBodies.add(typeConverter.convertTo(String.class, new File("src/test/resources/ac160001.dat_9780198790044.json")));
		expectedBodies.add(typeConverter.convertTo(String.class, new File("src/test/resources/ac160001.dat_9780192149770.json")));
		backupBiblioLocation.expectedBodiesReceivedInAnyOrder(expectedBodies);
		dropLocationActiveMQ.expectedBodiesReceivedInAnyOrder(expectedBodies);
		
		ftpserver.sendBodyAndHeader(body, "CamelFileName", "ac160001.dat");
		
		dropLocationActiveMQ.assertIsSatisfied();
		backupSAPLocation.assertIsSatisfied();
		backupBiblioLocation.assertIsSatisfied();
		
	}
	
	@Test
	public void invalidSAPDataType() throws InterruptedException {
		
		backupSAPLocation.expectedMessageCount(1);
		backupBiblioLocation.expectedMessageCount(0);
		backupErrorLocation.expectedMessageCount(1);
		
		String body = camelContext.getTypeConverter().convertTo(String.class, new File("src/test/resources/ac160002.dat"));
		backupSAPLocation.expectedBodiesReceived(body);
		
		ftpserver.sendBodyAndHeader(body, "CamelFileName", "ac160002.dat");
		
		backupSAPLocation.assertIsSatisfied();
		backupBiblioLocation.assertIsSatisfied();
		backupErrorLocation.assertIsSatisfied();
	}
	
	@Test
	public void invalidSAP() throws InterruptedException {
		
		backupSAPLocation.expectedMessageCount(1);
		backupBiblioLocation.expectedMessageCount(0);
		backupErrorLocation.expectedMessageCount(1);
		
		String body = "test";
		backupSAPLocation.expectedBodiesReceived(body);
		ftpserver.sendBodyAndHeader(body, "CamelFileName", "ac160003.dat");
			
		backupSAPLocation.assertIsSatisfied();
		backupBiblioLocation.assertIsSatisfied();
		backupErrorLocation.assertIsSatisfied();
		
	}

 }
