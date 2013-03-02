package jgopherd.core;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import java.util.logging.Level;

/**
 * TCP gopher listener thread.
 * 
 * @author Richard
 */
public class ListenerThread implements Runnable {
	public final ExecutorService pool = Executors.newFixedThreadPool(64, new ThreadFactory() {
		@Override
		public Thread newThread(Runnable runnable) {
			return new Thread(runnable, "jgopherd client #" + (threadCounter++));
		}
	});
	
	public static long threadCounter = 1;
	
	public ServerSocket listenSocket;
	
	@Override
	public void run() {
		Main.log.log(Level.INFO, "Binding on port: " + Main.config.port);
		try {
			listenSocket = new ServerSocket(Main.config.port);
		} catch (IOException e) {
			Main.log.log(Level.SEVERE, "Could not bind to port " + Main.config.port + ": " + e);
			return;
		}
		
		while (!Main.serverStopped) {
			try {
				Socket client = listenSocket.accept();
				pool.execute(new ClientThread(client));
			} catch (IOException e) {
				Main.log.log(Level.SEVERE, "Failed to accept connection: " + e);
			}
		}
	}
}
