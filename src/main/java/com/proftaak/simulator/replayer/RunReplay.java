package com.proftaak.simulator.replayer;

import com.proftaak.simulator.replayer.runners.LinkToCoordinateTransformer;
import com.proftaak.simulator.replayer.runners.Replayer;
import java.io.IOException;
import java.util.concurrent.TimeoutException;
import javax.xml.parsers.ParserConfigurationException;
import org.cts.crs.CRSException;
import org.cts.op.CoordinateOperationException;
import org.xml.sax.SAXException;

public class RunReplay
{
	public static void main(String[] args) throws IOException, ParserConfigurationException, SAXException, CRSException, CoordinateOperationException, TimeoutException
	{
		String inputFile = "output_events";
		long delay = (long) 0.05; // in ms

		LinkToCoordinateTransformer transfomer = new LinkToCoordinateTransformer("output_network");
		Replayer.run(inputFile, transfomer, delay);
		Replayer.run(inputFile, transfomer, delay);
		Replayer.run(inputFile, transfomer, delay);
		Replayer.run(inputFile, transfomer, delay);
	}
}
