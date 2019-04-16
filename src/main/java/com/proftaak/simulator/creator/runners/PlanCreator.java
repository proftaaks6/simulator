package com.proftaak.simulator.creator.runners;

import com.proftaak.simulator.creator.Gzip;
import com.proftaak.simulator.creator.models.Activity;
import com.proftaak.simulator.creator.models.Leg;
import com.proftaak.simulator.creator.models.Person;
import com.proftaak.simulator.creator.models.Plan;
import com.proftaak.simulator.creator.models.Population;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.StringWriter;
import java.util.Random;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;

public class PlanCreator
{
	private double minLong;
	private double maxLong;
	private double minLat;
	private double maxLat;

	private int minHourDeparture = 5;
	private int maxHourDeparture = 8;

	private int minHourReturn = 16;
	private int maxHourReturn = 19;

	private int planCount;

	private Population population;
	private Random r;

	public PlanCreator(int planCount, double minxX, double maxX, double minY, double maxY) {
		System.out.println("Creating plan with minimum and maximum coordinates");
		this.minLong = minxX;
		this.maxLong = maxX;
		this.minLat = minY;
		this.maxLat = maxY;
		this.planCount = planCount;

		population = new Population();

		r = new Random();
	}

	public PlanCreator create() {
		for (int i = 0; i < planCount; i++) {
			Person person = new Person(i);

			Activity home = new Activity();
			home.type = "home";
			home.x = minLong + (maxLong - minLong) * r.nextDouble();
			home.y = minLat + (maxLat - minLat) * r.nextDouble();
			String hour = Integer.toString(r.nextInt((maxHourDeparture - minHourDeparture) + 1) + minHourDeparture);
			String minute = Integer.toString(r.nextInt(59));
			if (minute.length() == 1) {
				minute = "0"+minute;
			}
			home.endTime = "0" + hour + ":" + minute + ":00";

			person.plan = new Plan();
			person.plan.activity1 = home;
			person.plan.activity3 = home;

			Activity work = new Activity();
			work.type = "work";
			work.x = minLong + (maxLong - minLong) * r.nextDouble();
			work.y = minLat + (maxLat - minLat) * r.nextDouble();
			hour = Integer.toString(r.nextInt((maxHourReturn - minHourReturn) + 1) + minHourReturn);
			minute = Integer.toString(r.nextInt(59));
			if (minute.length() == 1) {
				minute = "0"+minute;
			}
			work.endTime = hour + ":" + minute + ":00";

			person.plan.activity2 = work;
			person.plan.leg1 = new Leg();
			person.plan.leg2 = new Leg();

			population.addPerson(person);
		}

		System.out.println("Done");

		return this;
	}

	private void writeToFile() {
		try {
			JAXBContext jaxbContext = JAXBContext.newInstance(Population.class);
			Marshaller jaxbMarshaller = jaxbContext.createMarshaller();

			jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
			jaxbMarshaller.setProperty(Marshaller.JAXB_FRAGMENT, true);

			StringWriter sw = new StringWriter();
			sw.write("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
					"<!DOCTYPE population SYSTEM \"http://www.matsim.org/files/dtd/population_v6.dtd\">\n");
			jaxbMarshaller.marshal(population, sw);

			FileWriter fw = new FileWriter("./input/plans.xml");
			fw.write(sw.toString());
			fw.close();

			Gzip.compressGZIP(new File("./input/plans.xml"), new File("./input/plans.xml.gz"));

			System.out.println(sw.toString());

		} catch (JAXBException | IOException e)
		{
			e.printStackTrace();
		}
	}

	public static void run(NetworkReader networkReader, int planCount) {
		new PlanCreator(planCount, networkReader.minX, networkReader.maxX, networkReader.minY, networkReader.maxY)
				.create().writeToFile();
	}

}
