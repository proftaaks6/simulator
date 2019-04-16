package com.proftaak.simulator.creator.models;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class Population
{
	public List<Person> person = new ArrayList<>();

	public void addPerson(Person person) {
		this.person.add(person);
	}
}
