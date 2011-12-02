package com.viamep.richard.jgopherd;

import java.net.ServerSocket;

public class ServerListenThread extends Thread {
	public int listenport = 70;
	public ServerSocket socket;
	
	public ServerListenThread(int port) {
		listenport = port;
	}
	
	public void run() {
		try {
			socket = new ServerSocket(listenport);
		} catch (Throwable e) {
			Main.log.warning("Unable to listen on port "+listenport+": "+e.getMessage());
			return;
		}
		while (true) {
			try {
				ClientThread thread = new ClientThread(socket.accept());
				thread.start();
			} catch (Throwable e) {
				Main.log.warning("Accept failed on port "+listenport+": "+e.getMessage());
			}
		}
	}
}
