package edu.htwm.vsp.services.rest.impl;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.UriBuilder;
import javax.ws.rs.core.UriInfo;

import edu.htwm.vsp.services.birthday.core.BirthDayInfo;
import edu.htwm.vsp.services.birthday.core.BirthdayService;
import edu.htwm.vsp.services.rest.BirthdayResource;

public class BirthdayResourceImpl implements BirthdayResource {

	@Context
	private BirthdayService birthdayService;

	@Override
	public Response getBirthdayInformation(String name) {
		
		if(!birthdayService.hasSuggestionsFor(name)) {
			return Response.status(Status.NOT_FOUND).build();
		} 
		
		BirthDayInfo suggestionsFor = birthdayService.getSuggestionsFor(name);
		
		return Response.ok(suggestionsFor).build();
	}

	@Override
	public Response addSuggestion(
		UriInfo uriInfo,
		String name, int dayOfMonth, int month, String hobbies) {
		
		List<String> splittedHobbies = new ArrayList<String>();
		if(hobbies != null && hobbies.contains(",")) {
			for(String h :hobbies.split(",")) {
				splittedHobbies.add(h);
			}
		}
			
		birthdayService.addSuggestion(name, dayOfMonth, month, splittedHobbies);
		
		UriBuilder absolutePathBuilder = uriInfo.getAbsolutePathBuilder();
		URI created = absolutePathBuilder.path(BirthdayResource.class, "getBirthdayInformation").build(name);
		
		return Response.created(created).build();
	}
	
	
}
