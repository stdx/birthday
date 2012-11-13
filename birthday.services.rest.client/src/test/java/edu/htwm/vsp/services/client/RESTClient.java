package edu.htwm.vsp.services.client;

public abstract class RESTClient {

	private static final String _serviceBaseURI = "http://localhost:8080";
	
	private final String serviceBaseURI;

	public RESTClient() {
		this(_serviceBaseURI);
	}
	
	public RESTClient(String serviceBaseURI) {
		this.serviceBaseURI = serviceBaseURI;
	}

	public String getServiceBaseURI() {
		return serviceBaseURI;
	}
	
}
