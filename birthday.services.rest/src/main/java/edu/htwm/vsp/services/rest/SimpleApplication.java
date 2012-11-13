package edu.htwm.vsp.services.rest;

import java.util.HashSet;
import java.util.Set;

import javax.ws.rs.core.Application;
import javax.ws.rs.core.Context;

import com.sun.jersey.spi.inject.SingletonTypeInjectableProvider;

import edu.htwm.vsp.services.birthday.core.BirthdayService;
import edu.htwm.vsp.services.birthday.core.BirthdayServiceInMemory;
import edu.htwm.vsp.services.rest.impl.BirthdayResourceImpl;
import edu.htwm.vsp.services.rest.impl.GreetingsResourceImpl;


public class SimpleApplication extends Application {
	
	private final Set<Object> singletons = new HashSet<Object>();
	private final Set<Class<?>> resourcesToRegister = new HashSet<Class<?>>();
	private final BirthdayService birthdayService;
			
	public SimpleApplication() {
		
		// add the birthday service
		birthdayService = new BirthdayServiceInMemory();
		SingletonTypeInjectableProvider<Context, BirthdayService> birthdayServiceProvider 
			= new SingletonTypeInjectableProvider<Context, BirthdayService>(BirthdayService.class, birthdayService) {
		};
		singletons.add(birthdayServiceProvider);
		
	}
	
	@Override
	public Set<Class<?>> getClasses() {
		
		resourcesToRegister.add(GreetingsResourceImpl.class);
		resourcesToRegister.add(BirthdayResourceImpl.class);
		
		return resourcesToRegister;
	}
	
	@Override
	public Set<Object> getSingletons() {
		return singletons;
	}
}
