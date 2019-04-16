package com.proftaak.simulator.creator.runners;

import java.io.File;

public class Cleaner
{

	public static void run() {
		String[] fileNames = {"network.xml", "plans.xml"};
		for (String fileName : fileNames)
		{
			File file = new File("./input/" + fileName);
			file.delete();
		}
	}
}
