package com.proftaak.simulator.creator.runners;

import org.matsim.api.core.v01.network.Network;
import org.matsim.api.core.v01.network.NetworkWriter;
import org.matsim.core.network.NetworkUtils;
import org.matsim.core.network.algorithms.NetworkSimplifier;
import org.matsim.core.utils.geometry.CoordinateTransformation;
import org.matsim.core.utils.geometry.transformations.TransformationFactory;
import org.matsim.core.utils.io.OsmNetworkReader;
import org.matsim.run.NetworkCleaner;

public class NetworkCreator {

	public static void run(String inputFile) {
		String input = "./input/" + inputFile + ".osm";
		String output = "./input/network.xml.gz";
		CoordinateTransformation wgs84_utm32N = TransformationFactory.getCoordinateTransformation(TransformationFactory.WGS84, "EPSG:25832");
		Network network = NetworkUtils.createNetwork();
		OsmNetworkReader reader = new OsmNetworkReader(network, wgs84_utm32N);
		reader.setKeepPaths(true);
		reader.setMemoryOptimization(true);
		reader.parse(input);

		new NetworkSimplifier().run(network);
		new NetworkWriter(network).write(output);
		new NetworkCleaner().run(new String[]{output, output});
	}

}
