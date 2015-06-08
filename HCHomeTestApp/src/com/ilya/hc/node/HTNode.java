package com.ilya.hc.node;

import java.io.IOException;
import java.net.Socket;
import java.util.Map;

import com.hazelcast.client.HazelcastClient;
import com.hazelcast.client.config.ClientConfig;
import com.hazelcast.client.config.ClientNetworkConfig;
import com.hazelcast.config.Config;
import com.hazelcast.config.GroupConfig;
import com.hazelcast.config.InterfacesConfig;
import com.hazelcast.config.NetworkConfig;
import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;
import com.ilya.hc.exception.HTNodeInitException;

public class HTNode {

	private String login;
	private String password;
	private String address;
	private String name;
	private String hazelcastInstanceName;
	private int port;
	private HazelcastInstance instance;	

	/**
	 * HTNode is Home Task Node that represents one node.
	 * It connects to existing Hazelcast instance if there is any.
	 * Otherwise it launches new local instance.
	 * It does not launch new instance by default to lower memory footprint.
	 * 
	 * @param login for Hazelcast instance
	 * @param password for Hazelcast instance
	 * @param address of Hazelcast instance
	 * @param port of Hazelcast instance
	 * @param name of the instance
	 * @throws HTNodeInitException 
	 */
	public HTNode(String login, String password, String address, String nodeName, String port) throws HTNodeInitException {
		this.login = login;
		this.password = password;
		this.address = address;
		this.name = nodeName;
		this.port = Integer.parseInt(port);
		this.hazelcastInstanceName = "HomeTest";
		initHazelcastInstance();
		
	}

	/**
	 * Does System.out.println("We are started!"); if this node is the first one that started
	 * Otherwise just indicates that it is started
	 * 
	 */
	public void doTell() {
		Map<Integer, String> lockMap = instance.getMap("lockMap");
		
		String value = lockMap.put(0, "Initialized");
		//There is no need to lock key since put is thread safe and only the first guy will get null if key is absent
		//If value itself would be important, it would make sense to lock

		if (value == null) {
			//in three lines because it's explicitly said "System.out.println("We are started!")" and few \n to make it more visible
			System.out.println("\n\n");
			System.out.println("We are started!");
			System.out.println("\n\n");
			System.out.println(name);
		} else {
			//this node is not the first one, but it still would be interesting to know that node is started
			System.out.println("Yet another node is up. " + name);
		}
	}

	/**
	 * Initialize {@link #instance}
	 * Tries to connect to existing Hazelcast instance if {@link #address}:{@link #port} is busy
	 * Otherwise start new local Hazelcast node
	 * @throws HTNodeInitException 
	 * 
	 */
	private void initHazelcastInstance() throws HTNodeInitException {
		try {
			// checking whether port is open. It's few seconds faster than trying to join right away
			Socket s = new Socket(address, port);
			s.close();

			connectToHazelcastInstance();
		} catch (IOException e) {
			launchHazelcastInstance(false);
		}
	}

	/**
	 * Connects to Hazelcast Instance and @throws IllegalStateException in case it can't find it  
	 * Uses {@link #address}, {@link #port}, {@link #login}, {@link #password}
	 * 
	 * @throws HTNodeInitException when can't connect to Hazelcast instance
	 */
	private void connectToHazelcastInstance() throws HTNodeInitException {
		ClientConfig config = new ClientConfig();

		ClientNetworkConfig networkConfig = config.getNetworkConfig();
		networkConfig.addAddress(address + ":" + port);

		GroupConfig groupConfig = config.getGroupConfig();
		groupConfig.setName(login);
		groupConfig.setPassword(password);

		try {
			instance = HazelcastClient.newHazelcastClient(config);					
		} catch (Exception e) {
			throw new HTNodeInitException("Can't connect to Hazelcast instance. Check that " + address + ":" + port + " is not busy with something else.");
		}
	}

	/**
	 * Launches Hazelcast instance.
	 * Uses {@link #address}, {@link #port}, {@link #login}, {@link #password}
	 * 
	 * @param portAutoIncrement allow to auto-increment port if {@link #port} is busy  
	 * @throws HTNodeInitException when can't start new Hazelcast instance
	 */
	private void launchHazelcastInstance(boolean portAutoIncrement) throws HTNodeInitException {
		Config config = new Config(hazelcastInstanceName);
		
		NetworkConfig networkConfig = config.getNetworkConfig();
		
		InterfacesConfig interfacesConfig = networkConfig.getInterfaces(); 
		interfacesConfig.addInterface(address);
		
		networkConfig.setPort(port);
		networkConfig.setPortAutoIncrement(portAutoIncrement);

		GroupConfig groupConfig = config.getGroupConfig();
		groupConfig.setName(login);
		groupConfig.setPassword(password);

		try {
			instance = Hazelcast.newHazelcastInstance(config);			
		} catch (Exception e) {
			throw new HTNodeInitException("Can't start new Hazelcast instance.");
		}
	}

}
