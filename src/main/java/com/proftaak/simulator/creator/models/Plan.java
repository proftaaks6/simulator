package com.proftaak.simulator.creator.models;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class Plan
{
	// @XmlAttribute
	// public String selected = "yes";

	@XmlElement(name = "activity")
	public Activity activity1;
	@XmlElement(name = "leg")
	public Leg leg1;
	@XmlElement(name = "activity")
	public Activity activity2;
	@XmlElement(name = "leg")
	public Leg leg2;
	@XmlElement(name = "activity")
	public Activity activity3;
}
