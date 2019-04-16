package com.proftaak.simulator.creator.models;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class Person
{
	@XmlAttribute
	public int id;

	public Plan plan;

	public Person() { }

	public Person(int id) {
		this.id = id;
	}
}
