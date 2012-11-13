package edu.htwm.vsp.services.client;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringReader;
import java.io.Writer;
import java.net.HttpURLConnection;

import javax.naming.NamingException;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.methods.GetMethod;
import org.codehaus.jackson.map.ObjectMapper;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import edu.htwm.vsp.services.birthday.core.BirthDayInfo;


/**
 * Shows how Apache HttpClient can be used to build a simple http REST client. 
 * 
 * @author hol
 */
public class HttpComponentsClient extends RESTClient {


	
	@BeforeClass
	public static void setUpClass() {
	}

	
	@Before
	public void setUp() throws NamingException {
	}


	// -------------------------------------------------------------------------------
	// 								helper functions
	// -------------------------------------------------------------------------------
	
	/**
	 * Example code for serializing a BirthdayInfo via JAXB.
	 */
	private void marshalBirthDayInfoToXML(BirthDayInfo b, Writer out) throws JAXBException {
		
		JAXBContext jc = JAXBContext.newInstance(BirthDayInfo.class);
		assertNotNull(jc);
		Marshaller marshaller = jc.createMarshaller();
		assertNotNull(marshaller);
		marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, new Boolean(true));
		marshaller.marshal(b, out);
	}

	
	/**
	 * Example code for serializing a BirthdayInfo via jackson.
	 */
	private void marshalBirthDayInfoToJSON(BirthDayInfo b, Writer out) throws Exception {
		 ObjectMapper mapper = new ObjectMapper();
		 mapper.writeValue(out, b);
	}
	
	
	/**
	 * Example code for deserializing a BirthDayInfo via JAXB.
	 */
	private BirthDayInfo unmarshalBirthDayInfoFromXML(Reader in) throws JAXBException {
		
		JAXBContext jc = JAXBContext.newInstance(BirthDayInfo.class);
		assertNotNull(jc);
		Unmarshaller unmarshaller = jc.createUnmarshaller();
		assertNotNull(unmarshaller);
		Object unmarshalledObj = unmarshaller.unmarshal(in);
		assertNotNull(unmarshalledObj);
		assertTrue(unmarshalledObj instanceof BirthDayInfo);
		return (BirthDayInfo) unmarshalledObj;
	}	
	

	/**
	 * Example code for deserializing a BirthDayInfo via jackson.
	 */
	private BirthDayInfo unmarshalBirthDayInfoFromJSON(Reader in) throws Exception {
		 ObjectMapper mapper = new ObjectMapper();
		 return mapper.readValue(in, BirthDayInfo.class);
	}	

	
	
	private void verifyGreetingsForName(String name, int expectedStatusCode) throws IOException {

		System.out.println("\nrequest greetings for name " + name + " ...");
		
		HttpClient client = new HttpClient();
		
		// -- build HTTP GET request
		HttpMethod method = new GetMethod(getServiceBaseURI() + "/greetings/" + name);
		System.out.println("HTTP get resource path: " + method.getPath());
		
		int responseCode = client.executeMethod(method);
		
		// -- verify that the service responds with the expected HTTP status code  
		System.out.println("HTTP response code: " + responseCode);
		assertEquals(expectedStatusCode, responseCode);	

		if( expectedStatusCode !=  HttpURLConnection.HTTP_OK) {
			return;
		}

		// -- read http response body
		BufferedReader reader = new BufferedReader(new InputStreamReader(method.getResponseBodyAsStream()));
		System.out.println("HTTP response body:");
		StringBuffer s = new StringBuffer();
		String line = reader.readLine();
		while (line != null) {
			s.append(line);
			s.append('\n');
			line = reader.readLine();
		}
		System.out.println("\t" + s.toString());
	}
	
	
	private BirthDayInfo verifyBirthDayInfoForName(String name, int expectedStatusCode, String contentType) throws Exception {

		System.out.println("\nrequest birthday info for name " + name + " ...");
		
		HttpClient client = new HttpClient();
		
		// -- build HTTP GET request
		HttpMethod method = new GetMethod(getServiceBaseURI() + "/birthdays/" + name);
		System.out.println("HTTP get resource path: " + method.getPath());
		
		// -- determine the mime type you wish to receive here 
		method.setRequestHeader("Accept", contentType);

		int responseCode = client.executeMethod(method);
		
		// -- verify that the service responds with the expected HTTP status code  
		System.out.println("HTTP response code: " + responseCode);
		assertEquals(expectedStatusCode, responseCode);	

		if( expectedStatusCode !=  HttpURLConnection.HTTP_OK) {
			return null;
		}
		
		System.out.println("response Content-type: " + method.getResponseHeader("Content-Type").getValue());
		
		// -- read http response body
		BufferedReader reader = new BufferedReader(new InputStreamReader(method.getResponseBodyAsStream()));
		System.out.println("HTTP response body:");
		StringBuffer s = new StringBuffer();
		String line = reader.readLine();
		while (line != null) {
			s.append(line);
			s.append('\n');
			line = reader.readLine();
		}
		System.out.println("\t" + s.toString());
		
		BirthDayInfo b;
		
		if(  method.getResponseHeader("Content-Type").getValue().equals("application/json") ) {
			b = unmarshalBirthDayInfoFromJSON(new StringReader(s.toString()));
		}
		else {
			b = unmarshalBirthDayInfoFromXML(new StringReader(s.toString()));
		}
		return b;
	}

	
	// -------------------------------------------------------------------------------
	// 								test cases
	// -------------------------------------------------------------------------------
	
	
	@Test
	public void requestGreetingsTest() throws Exception {
		verifyGreetingsForName("Heinz", HttpURLConnection.HTTP_OK);
	}

	
	@Test
	public void requestBirthDayInfoTest() throws Exception {
		String name;
		BirthDayInfo result;
		
//		name = "Anne";
//		result = verifyBirthDayInfoForName(name, HttpURLConnection.HTTP_OK, "application/json");
//		System.out.println("Birthday info for " + name + ": " + result);
		
		name = "Peter";
		result = verifyBirthDayInfoForName(name, HttpURLConnection.HTTP_OK, "application/xml");
		System.out.println("Birthday info for " + name + ": " + result);

		name = "Peter";
		result = verifyBirthDayInfoForName(name, HttpURLConnection.HTTP_NOT_ACCEPTABLE, "bla");

		name = "Klaus";
		result = verifyBirthDayInfoForName(name, HttpURLConnection.HTTP_NOT_FOUND, "application/xml");
	}
	
}
