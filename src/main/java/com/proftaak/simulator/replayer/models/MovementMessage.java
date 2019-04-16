package com.proftaak.simulator.replayer.models;

public class MovementMessage
{
	public long trackerId;
	public Coordinate coordinate;

	@Override
	public String toString()
	{
		return "MovementMessage{" +
				"trackerId=" + trackerId +
				", coordinate=" + coordinate +
				'}';
	}
}
