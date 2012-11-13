package edu.htwm.vsp.services.birthday.core;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BirthdayServiceInMemory implements BirthdayService {

	public static final String INVALID_NAME = "invalid";
	
	private Map<String, BirthDayInfo> birthdayInfoByName;

	public BirthdayServiceInMemory() {
		
		birthdayInfoByName = new HashMap<String, BirthDayInfo>();
		
		// fill up with some example data
		birthdayInfoByName.put("anne", new BirthDayInfo(21, 11, "belletristik", "jazz"));
		birthdayInfoByName.put("horst", new BirthDayInfo(2, 1, "autos", "reisen", "wein"));
		birthdayInfoByName.put("peter", new BirthDayInfo(31, 3, "hunde", "orchideen", "astrologie"));
		birthdayInfoByName.put("emil", new BirthDayInfo(10, 4, "esoterik", "hip-hop"));
	}
	
	@Override
	public synchronized BirthDayInfo getSuggestionsFor(String name) {
		return birthdayInfoByName.get(toInternalName(name).toLowerCase());
	}
	
	@Override
	public synchronized  boolean hasSuggestionsFor(String name) {
		String internalName = toInternalName(name);
		
		return this.birthdayInfoByName.containsKey(internalName.toLowerCase());
	}
	
	@Override
	public synchronized BirthDayInfo addSuggestion(String name, int dayOfMonth, int month, List<String> hobbies) {
		if(INVALID_NAME.equals(name))
			return null;
		
		String internalName = toInternalName(name);
		BirthDayInfo birthDayInfo = new BirthDayInfo(dayOfMonth, month, hobbies);
		birthdayInfoByName.put(internalName, birthDayInfo);
		
		return new BirthDayInfo(birthDayInfo);
		
	}

	
	private String toInternalName(String externalName) {
		if(externalName  == null || externalName.isEmpty() || externalName.length() == 0) {
			return INVALID_NAME;
		} else {
			return externalName.toLowerCase();
		}
	}
}
