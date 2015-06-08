package com.ilya.hc.node;

import java.util.Map;

import com.hazelcast.config.Config;
import com.hazelcast.config.GroupConfig;
import com.hazelcast.config.InterfacesConfig;
import com.hazelcast.config.NetworkConfig;
import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;

public class HTNode {

	private String login;
	private String password;
	private String address;
	private int port;
	private String name;
	private String hazelcastInstanceName;
	private HazelcastInstance instance;	

	/**
	 * HTNode is Home Task Node that represents one node.
	 * It launches Hazelcast instance.
	 * 
	 * @param login for Hazelcast instance
	 * @param password for Hazelcast instance
	 * @param address of Hazelcast instance
	 * @param port of Hazelcast instance
	 * @name name of the node
	 */
	public HTNode(String login, String password, String address, String nodeName, String port) {
		this.login = login;
		this.password = password;
		this.address = address;
		this.name = nodeName;
		this.port = Integer.parseInt(port);
		this.hazelcastInstanceName = "HomeTest";
		launchHazelcastInstance();
	}

	/**
	 * Does System.out.println("We are started!"); if this node is the first one that started
	 * Otherwise just indicates that it is started
	 * 
	 */
	public void doTell() {
		Map<Integer, String> lockMap = instance.getMap("lockMap");
		
		//There is no need to lock key since put is thread safe and only the first guy will get null if key is absent
		//If value itself would be important, it would make sense to lock
		String value = lockMap.put(0, "Initialized");

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
	 * Launches Hazelcast instance.
	 * Uses {@link hazelcastInstanceName} {@link #address}, {@link #port}, {@link #login}, {@link #password}
	 * 
	 */
	private void launchHazelcastInstance() {
		Config config = new Config(hazelcastInstanceName);

		NetworkConfig networkConfig = config.getNetworkConfig();
		networkConfig.setPort(port);
		
		InterfacesConfig interfacesConfig = networkConfig.getInterfaces(); 
		interfacesConfig.addInterface(address);

		GroupConfig groupConfig = config.getGroupConfig();
		groupConfig.setName(login);
		groupConfig.setPassword(password);

		instance = Hazelcast.newHazelcastInstance(config);	
	}

}
