package edu.htwm.vsp.services.client;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertTrue;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringReader;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLDecoder;
import java.net.URLEncoder;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import org.codehaus.jackson.map.ObjectMapper;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import edu.htwm.vsp.services.birthday.core.BirthDayInfo;




/**
 * Shows how java.net.*, java.io.* classes, and JAXB API can be used to build a simple http REST client. 
 * 
 * @author hol
 */
public class SimpleJavaHttpClient extends RESTClient {
	
	private Logger log = LoggerFactory.getLogger(SimpleJavaHttpClient.class);

	

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
		
		name = URLEncoder.encode(name, "UTF-8");
		
		// -- build REST request URL
		URL requestUrl = new URL(getServiceBaseURI() + "/greetings/" + name);

		// -- build a HTTP GET request
		HttpURLConnection connection = (HttpURLConnection) requestUrl.openConnection();
		System.out.println("HTTP get request url: " + connection.getURL());
		connection.setRequestMethod("GET");
		
		// -- compute http response
		
		// -- verify that the service responds with the expected HTTP status code  
		System.out.println("HTTP response code: " + connection.getResponseCode() + "(" + connection.getResponseMessage() + ")");
		assertEquals(expectedStatusCode, connection.getResponseCode());	

		if( expectedStatusCode !=  HttpURLConnection.HTTP_OK) {
			return;
		}

		// -- read http response body
		BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
		System.out.println("HTTP response body:");
		StringBuffer s = new StringBuffer();
		String line = reader.readLine();
		while (line != null) {
			s.append(line);
			line = reader.readLine();
		}
		System.out.println("\t" + s.toString());
		connection.disconnect();
	}

	
	private BirthDayInfo verifyBirthDayInfoForName(String name, int expectedStatusCode, String contentType) throws Exception {

		System.out.println("\nrequest bday info for name " + name + " ...");
		
		// -- build REST request URL
		URL requestUrl = new URL(getServiceBaseURI() + "/birthdays/" + name);

		// -- build a HTTP GET request
		HttpURLConnection connection = (HttpURLConnection) requestUrl.openConnection();
		
		// -- determine the mime type you wish to receive here 
		connection.setRequestProperty("Accept", contentType);

		System.out.println("HTTP get request url: " + connection.getURL());
		connection.setRequestMethod("GET");
		connection.setDoOutput(true);
		
		// -- compute http response
		
		// -- verify that the service responds with the expected HTTP status code  
		System.out.println("HTTP response code: " + connection.getResponseCode() + "(" + connection.getResponseMessage() + ")");
		assertEquals(expectedStatusCode, connection.getResponseCode());	
		
		System.out.println("response Content-type: " + connection.getContentType());
		
		if( expectedStatusCode !=  HttpURLConnection.HTTP_OK) {
			return null;
		}
		
		// -- read http response body
		BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
		System.out.println("HTTP response body:");
		StringBuffer s = new StringBuffer();
		String line = reader.readLine();
		while (line != null) {
			s.append(line);
			line = reader.readLine();
		}
		System.out.println("\t" + s.toString());
		
		connection.disconnect();

		BirthDayInfo b;
		
		if( connection.getContentType().equals("application/json") ) {
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
		verifyGreetingsForName("Bär", HttpURLConnection.HTTP_OK);
		verifyGreetingsForName("Vögel", HttpURLConnection.HTTP_OK);
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

	
	@Test
	public void test() throws UnsupportedEncodingException {
		String inputStr = "Vögel"; 
		log.info("inputStr: " + inputStr);
		String encStr = URLEncoder.encode(inputStr, "UTF-8");
		log.info("encStr: " + encStr);
		String decStr = URLDecoder.decode(encStr, "ISO-8859-1");
//		decStr = URLEncoder.encode(decStr, "UTF-8");
//		decStr = URLDecoder.decode(encStr, "UTF-8");
		decStr = new String(decStr.getBytes("ISO-8859-1"));
		log.info("decStr: " + decStr);
	}

	
}

