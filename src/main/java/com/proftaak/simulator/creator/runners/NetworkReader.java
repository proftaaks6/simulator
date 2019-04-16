package com.proftaak.simulator.creator.runners;

import com.proftaak.simulator.creator.Gzip;
import java.io.File;
import java.io.IOException;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public class NetworkReader
{
	public double minX, minY, maxX, maxY;

	private NetworkReader readNetwork(File input) throws ParserConfigurationException, IOException, SAXException
	{
		System.out.println("Reading network for minimum and maximum coordinates");

		SAXParserFactory factory = SAXParserFactory.newInstance();
		SAXParser saxParser = factory.newSAXParser();

		DefaultHandler handler = new DefaultHandler() {

			boolean isFirst = true;

			@Override
			public void startElement(String uri, String localName, String qName, Attributes attributes)
			{
				if (qName.equalsIgnoreCase("NODE")) {
					double x = Double.parseDouble(attributes.getValue("x"));
					double y = Double.parseDouble(attributes.getValue("y"));

					if (isFirst) {
						minX = x;
						maxX = x;
						minY = y;
						maxY = y;
						isFirst = false;
					}

					if (x < minX) {
						minX = x;
					}
					if (x > maxX) {
						maxX = x;
					}
					if (y < minY) {
						minY = y;
					}
					if (y > maxY) {
						maxY = y;
					}
				}

			}

		};

		saxParser.parse(input, handler);

		System.out.println("Done");
		return this;
	}

	public static NetworkReader run(String inputFile) throws IOException, ParserConfigurationException, SAXException
	{
		String input = "./input/" + inputFile + ".xml.gz";
		String output = "./input/" + inputFile + ".xml";

		Gzip.decompressGzip(new File(input), new File(output));

		return new NetworkReader().readNetwork(new File(output));
	}
}
