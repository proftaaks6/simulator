package com.proftaak.simulator.replayer;

import com.proftaak.simulator.replayer.runners.LinkToCoordinateTransformer;
import com.proftaak.simulator.replayer.runners.Replayer;
import java.io.IOException;
import javax.xml.parsers.ParserConfigurationException;
import org.cts.crs.CRSException;
import org.cts.op.CoordinateOperationException;
import org.xml.sax.SAXException;

public class RunReplay
{
	public static void main(String[] args) throws IOException, ParserConfigurationException, SAXException, CRSException, CoordinateOperationException
	{
		String inputFile = "output_events";
		int delay = 100; // in ms

		Replayer.run(inputFile, new LinkToCoordinateTransformer("output_network"), delay);
	}
}
