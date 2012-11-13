package edu.htwm.vsp.services.rest.impl;

import javax.ws.rs.core.Response;

import edu.htwm.vsp.services.rest.GreetingsResource;

public class GreetingsResourceImpl implements GreetingsResource {

	
	@Override
	public Response sayHello(String name) {
		
		HelloInfo helloInfo = new HelloInfo(name);
		
		Response r = Response.ok(helloInfo).build();
		return r;
	}
	
}
