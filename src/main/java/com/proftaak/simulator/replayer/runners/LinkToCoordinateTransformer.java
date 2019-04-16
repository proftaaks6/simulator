package com.proftaak.simulator.replayer.runners;

import com.proftaak.simulator.creator.Gzip;
import com.proftaak.simulator.replayer.models.Coordinate;
import com.proftaak.simulator.replayer.models.Link;
import com.proftaak.simulator.replayer.models.Node;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import org.cts.CRSFactory;
import org.cts.IllegalCoordinateException;
import org.cts.crs.CRSException;
import org.cts.crs.CoordinateReferenceSystem;
import org.cts.crs.GeodeticCRS;
import org.cts.op.CoordinateOperation;
import org.cts.op.CoordinateOperationException;
import org.cts.op.CoordinateOperationFactory;
import org.cts.registry.EPSGRegistry;
import org.cts.registry.RegistryManager;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public class LinkToCoordinateTransformer
{
	private Map<String, Node> nodes = new HashMap<>();
	private Map<String, Link> links = new HashMap<>();

	public LinkToCoordinateTransformer(String inputFile) throws ParserConfigurationException, SAXException, IOException, CRSException, CoordinateOperationException
	{
		System.out.println("Converting network to global coordinate system...");

		File input = new File("./output/"+inputFile+".xml.gz");
		File output = new File("./output/"+inputFile+".xml");

		Gzip.decompressGzip(input, output);

		SAXParserFactory factory = SAXParserFactory.newInstance();
		SAXParser saxParser = factory.newSAXParser();

		CRSFactory cRSFactory = new CRSFactory();
		RegistryManager registryManager = cRSFactory.getRegistryManager();
		registryManager.addRegistry(new EPSGRegistry());
		CoordinateReferenceSystem from = cRSFactory.getCRS("EPSG:25832");
		CoordinateReferenceSystem to = cRSFactory.getCRS("EPSG:4326");

		Set<CoordinateOperation> coordOps = CoordinateOperationFactory.createCoordinateOperations((GeodeticCRS) from, (GeodeticCRS) to);

		// Note that we get a List and not a single CoordinateTransformation, because several methods may exist to
		// transform a position from crs1 to crs2

		DefaultHandler handler = new DefaultHandler() {
			@Override
			public void startElement(String uri, String localName, String qName, Attributes attributes)
			{
				if (qName.equalsIgnoreCase("NODE")) {
					// <node id="1002351266" x="254370.18480122832" y="5729806.1909024725" >
					String id = attributes.getValue("id");
					double fromX = Double.parseDouble(attributes.getValue("x"));
					double fromY = Double.parseDouble(attributes.getValue("y"));

					double[] coord = {fromX, fromY};

					if (!coordOps.isEmpty()) {
						Coordinate coordinate = null;
						for (CoordinateOperation op : coordOps) {
							if (!op.getAuthorityKey().equals("40")) {
								continue;
							}
							try
							{
								op.transform(coord);
								coordinate = new Coordinate();
								coordinate.longitude = coord[1];
								coordinate.latitude = coord[0];
								// Only first operation gives good results
								break;
							} catch (IllegalCoordinateException | CoordinateOperationException ignored) { }
						}

						if (coordinate != null) {
							Node node = new Node();
							node.id = id;
							node.coordinate = coordinate;
							nodes.put(id, node);
						}
					}
				}

				if (qName.equalsIgnoreCase("LINK")) {
					//		<link id="179079" from="42781128" to="42783264" length="77.64543696426216" freespeed="4.166666666666667" capacity="600.0" permlanes="1.0" oneway="1" modes="car" >
					//			<attributes>
					//				<attribute name="origid" class="java.lang.String" >7144076</attribute>
					//				<attribute name="type" class="java.lang.String" >residential</attribute>
					//			</attributes>
					//		</link>

					String id = attributes.getValue("id");
					String from = attributes.getValue("from");
					String to = attributes.getValue("to");
					double length = Double.parseDouble(attributes.getValue("length"));

					if (nodes.containsKey(from) && nodes.containsKey(to)) {
						Node fromNode = nodes.get(from);
						Node toNode = nodes.get(to);

						Link link = new Link();
						link.id = id;
						link.to = toNode;
						link.from = fromNode;
						link.length = length;

						links.put(id, link);
					}

				}

			}

		};

		saxParser.parse(output, handler);

		System.out.println("Done converting. Found " + nodes.size() + " nodes and " + links.size() + " links.");
	}

	public Coordinate transform(String linkId) {
		if (links.containsKey(linkId)) {
			Link link = links.get(linkId);
			if (nodes.containsKey(link.from.id)) {
				return nodes.get(link.from.id).coordinate;
			}
		}

		return null;
	}
}
