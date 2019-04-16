package com.proftaak.simulator.creator.models;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class Activity
{
	@XmlAttribute
	public String type;

	@XmlAttribute
	public double x;
	@XmlAttribute
	public double y;

	@XmlAttribute(name = "end_time")
	public String endTime;
}
