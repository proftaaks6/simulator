package com.proftaak.simulator.replayer.runners;

import com.proftaak.simulator.creator.Gzip;
import com.proftaak.simulator.replayer.models.Coordinate;
import com.proftaak.simulator.replayer.models.EventType;
import java.io.File;
import java.io.IOException;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public class Replayer
{
	File file;
	int delay;

	LinkToCoordinateTransformer transformer;

	public Replayer(File inputFile, LinkToCoordinateTransformer transformer, int delay)
	{
		this.file = inputFile;
		this.delay = delay;
		this.transformer = transformer;
	}

	private void runReplay() throws ParserConfigurationException, SAXException, IOException
	{
		System.out.println("Replaying simulated events...");

		SAXParserFactory factory = SAXParserFactory.newInstance();
		SAXParser saxParser = factory.newSAXParser();

		DefaultHandler handler = new DefaultHandler() {
			EventType type;

			@Override
			public void startElement(String uri, String localName, String qName, Attributes attributes)
			{
				if (attributes.getValue("type") == null){
					type = null;
					return;
				}

				type = EventType.valueOf(attributes.getValue("type").replace(" ", "_"));

				if (type == EventType.entered_link) {
					int vehicle = Integer.parseInt(attributes.getValue("vehicle"));
					String linkId = attributes.getValue("link");

					Coordinate coordinate = transformer.transform(linkId);

					System.out.println(coordinate);
					// TODO: Send to registrationSystem

					try
					{
						Thread.sleep(delay);
					} catch (InterruptedException e)
					{
						e.printStackTrace();
					}
				}
			}

		};

		saxParser.parse(file, handler);

		System.out.println("Done");
	}

	public static void run(String inputFile, LinkToCoordinateTransformer transformer, int delay) throws IOException, ParserConfigurationException, SAXException
	{
		String input = "./output/" + inputFile + ".xml.gz";
		String output = "./output/" + inputFile + ".xml";

		Gzip.decompressGzip(new File(input), new File(output));

		new Replayer(new File(output), transformer, delay).runReplay();
	}

}
