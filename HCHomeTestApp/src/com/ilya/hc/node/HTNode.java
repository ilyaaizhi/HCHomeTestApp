package com.ilya.hc.node;

public class HTNode {

	private String login;
	private String password;
	
	/**
	 * HTNode is Home Task Node that represents one node.
	 * If this node is the first one it says "We are started!"
	 * 
	 * @param login
	 * @param password
	 */
	public HTNode(String login, String password) {
		this.login = login;
		this.password = password;
	}

	/**
	 * Does System.out.println("We are started!"); if this node it the first one
	 * Otherwise just indicates that it has been started.
	 */
	public void doTell() {
		if (amITheFirst()) {
			System.out.println("We are started!");			
		} else {
			System.out.println("Yet another node is up.");
		}
	}
	
	private boolean amITheFirst() {
		return true;
	}

}
