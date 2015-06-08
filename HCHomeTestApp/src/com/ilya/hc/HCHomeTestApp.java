package com.ilya.hc;

import com.ilya.hc.node.HTNode;

/**
 * HCHomeTestApp is Hazelcast Home Test Application that can be launched in 
 * many nodes, but only one of them does
 * 	System.out.println("We are started!").
 * 
 * Other nodes acknowledge user by saying "Yet another node is up."
 * 
 * @author ilya
 *
 */

public class HCHomeTestApp {

	private static String login = "admin";
	private static String password = "AdminPassword";
	private static String address = "127.0.0.1";
	private static String port = "7775";
	private static boolean dieYong = false;
	private static String name = "";

	/**
	 * Entry point
	 * 
	 * @param args - application arguments
	 */
	public static void main(String[] args) {
		readParams(args);
		
		HTNode instance = new HTNode(login, password, address, name, port);
		
		// Simulation of instance crash right after Hazelcast instance initialization
		if (dieYong) System.exit(1);
		
		instance.doTell();
	}
	
	/**
	 * Read application arguments
	 * 
	 * @param args
	 */
	private static void readParams(String[] args) {
		try {
			for (int i = 0; i < args.length; ++i) {
				if (args[i].equals("-login")) {
					login = args[++i];
				} else if (args[i].equals("-password")) {
					password = args[++i];
				} else if (args[i].equals("-address")) {
					address = args[++i];
				} else if (args[i].equals("-port")) {
					port = args[++i];
				} else if (args[i].equals("-dieYong")) {
					dieYong = true;
				} else if (args[i].equals("-name")) {
					name = args[++i];
				} else {
					throw new Exception("Invalid argumet: " + args[i]);
				}
			}
		} catch (Exception e) {
			System.err.println(e);
			System.err.println("Usage: java -jar HCHomeTestApp.jar [-login <login>] [-password <password>] [-address <ip address>] [-port <port number>]");
		}
	}

}
