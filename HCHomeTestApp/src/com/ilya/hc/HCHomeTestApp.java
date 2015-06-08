package com.ilya.hc;

import com.ilya.hc.node.HTNode;

/**
 * HCHomeTestApp is Hazelcast Home Test Application that can be launched in 
 * many nodes, but only one of them does
 * 	System.out.println("We are started!").
 * 
 * @author ilya
 *
 */

public class HCHomeTestApp {

	private static String login = "admin";
	private static String password = "AdminPassword";

	/**
	 * Entry point
	 * 
	 * @param args - application arguments
	 */
	public static void main(String[] args) {
		readParams(args);

		HTNode node = new HTNode(login, password);
		node.doTell();
	}
	
	/**
	 * Read application arguments
	 * 
	 * @param args
	 */
	private static void readParams(String[] args) {
		for (int i = 0; i < args.length; ++i) {
			if (args[i].equals("-login")) {
				login = args[++i];
			} else if (args[i].equals("-password")) {
				password = args[++i];
			} else {
				System.out.println("Usage: java -jar HCHomeTestApp.jar [-login <login>] [-password <password>]");
			}
		}
		
	}

}
