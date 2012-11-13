package edu.htwm.vsp.services.rest.impl;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "hello")
public class HelloInfo {
	
	private String to;
	
	public HelloInfo() {
		this("anonymous");
	}
	
	public HelloInfo(String to) {
		setTo(to);
	}

	public String getTo() {
		return to;
	}

	public void setTo(String to) {
		this.to = to;
	}
	
}
