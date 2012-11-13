package edu.htwm.vsp.services.birthday.core;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlRootElement(name = "birthday")
@XmlType(propOrder = {"day", "month", "hobbies"})
public class BirthDayInfo {

	private int day;
	private int month;
	private List<String> hobbies;

	public BirthDayInfo(int dayOfMonth, int month, String ... hobbies) {
		this(dayOfMonth, month, Arrays.asList(hobbies));
	}
	
	public BirthDayInfo(int dayOfMonth, int month, List<String> hobbies) {
		this.setDay(dayOfMonth);
		this.setMonth(month);
		this.hobbies = new ArrayList<String>(hobbies.size());
		for(String h : hobbies)
			this.hobbies.add(h);
	}
	
	public BirthDayInfo() {
		this(1,1);
	}

	public BirthDayInfo(BirthDayInfo birthDayInfo) {
		this(birthDayInfo.getDay(), birthDayInfo.getMonth(), birthDayInfo.getHobbies());
	}

	public int getDay() {
		return day;
	}

	public void setDay(int day) {
		this.day = day;
	}

	public int getMonth() {
		return month;
	}

	public void setMonth(int month) {
		this.month = month;
	}

	@XmlElement(name = "hobby")
	@XmlElementWrapper(name = "hobbies")
	public List<String> getHobbies() {
		return hobbies;
	}

	public void setHobbies(List<String> hobbies) {
		this.hobbies = hobbies;
	}
	

}
