package com.proftaak.simulator.replayer.runners;

import com.google.gson.Gson;
import com.proftaak.simulator.creator.Gzip;
import com.proftaak.simulator.replayer.models.Coordinate;
import com.proftaak.simulator.replayer.models.EventType;
import com.proftaak.simulator.replayer.models.MovementMessage;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import java.io.File;
import java.io.IOException;
import java.util.concurrent.TimeoutException;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public class Replayer
{
	private static final String QUEUE_NAME = "Simulation_To_MovementProxy";
	private static final Gson gson = new Gson();

	File file;
	long delay;

	LinkToCoordinateTransformer transformer;

	Connection connection;
	Channel channel;


	public Replayer(File inputFile, LinkToCoordinateTransformer transformer, long delay) throws IOException, TimeoutException
	{
		this.file = inputFile;
		this.delay = delay;
		this.transformer = transformer;

		ConnectionFactory factory = new ConnectionFactory();
		connection = factory.newConnection();
		channel = connection.createChannel();
		this.channel.queueDeclare(QUEUE_NAME, false, false, false, null);
	}

	private void runReplay() throws ParserConfigurationException, SAXException, IOException
	{
		System.out.println("Replaying simulated events with "+delay+"ms delay...");

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
					MovementMessage message = new MovementMessage();
					message.trackerId = vehicle;
					message.coordinate = coordinate;

					try
					{
						channel.basicPublish("", QUEUE_NAME, null, gson.toJson(message).getBytes());
						//System.out.println("Send message:" + message);
					} catch (IOException ignored) { }

//					try
//					{
//						Thread.sleep(delay);
//					} catch (InterruptedException ignored) { }
				}
			}

		};

		saxParser.parse(file, handler);

		System.out.println("Done");
	}

	public static void run(String inputFile, LinkToCoordinateTransformer transformer, long delay) throws IOException, ParserConfigurationException, SAXException, TimeoutException
	{
		String input = "./output/" + inputFile + ".xml.gz";
		String output = "./output/" + inputFile + ".xml";

		Gzip.decompressGzip(new File(input), new File(output));

		new Replayer(new File(output), transformer, delay).runReplay();
	}

}
