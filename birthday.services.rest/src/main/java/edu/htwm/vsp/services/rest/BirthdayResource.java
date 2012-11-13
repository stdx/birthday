package edu.htwm.vsp.services.rest;

import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

@Path("birthdays")
public interface BirthdayResource {

	@GET
	@Path("{name}")
	@Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
	Response getBirthdayInformation(
		@PathParam("name") String name);
	
	@POST
	Response addSuggestion(
		@Context UriInfo uriInfo,
		@FormParam("name") String name,
		@FormParam("day")int dayOfMonth,
		@FormParam("month")int month,
		@FormParam("hobby") String hobbies);
	
	
}
