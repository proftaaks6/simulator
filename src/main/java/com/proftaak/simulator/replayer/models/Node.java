package com.proftaak.simulator.replayer.models;

public class Node
{
	public String id;
	public Coordinate coordinate;

	@Override
	public String toString()
	{
		return "Long: " + coordinate.longitude + " Lat: " + coordinate.lattitude;
	}
}
