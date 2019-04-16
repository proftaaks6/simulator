package com.proftaak.simulator.creator;

import com.proftaak.simulator.creator.runners.Cleaner;
import com.proftaak.simulator.creator.runners.NetworkCreator;
import com.proftaak.simulator.creator.runners.NetworkReader;
import com.proftaak.simulator.creator.runners.PlanCreator;
import java.io.IOException;
import javax.xml.parsers.ParserConfigurationException;
import org.xml.sax.SAXException;

public class RunCreator
{
	public static void main(String[] args) throws ParserConfigurationException, SAXException, IOException
	{
		NetworkCreator.run("noord-brabant");
		PlanCreator.run(NetworkReader.run("network"), 1000);
		Cleaner.run();
	}
}
