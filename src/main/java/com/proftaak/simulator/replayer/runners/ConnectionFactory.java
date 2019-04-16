package com.proftaak.simulator.replayer.runners;

import com.rabbitmq.client.Address;
import com.rabbitmq.client.AddressResolver;
import com.rabbitmq.client.Connection;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeoutException;

public class ConnectionFactory extends com.rabbitmq.client.ConnectionFactory {
	public ConnectionFactory(){
		super();
		setUsername("guest");
		setPassword("guest");
		setVirtualHost("/");
	}

	public Connection newConnection() throws IOException, TimeoutException
	{

		Address address1 = new Address("localhost", 5672);
		Address a2 = new Address("localhost", 5673);
		List<Address> list = new ArrayList<>();
		list.add(address1);
		list.add(a2);

		AddressResolver addressResolver = createAddressResolver(list);
		return newConnection(addressResolver);

	}

}