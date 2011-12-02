package com.viamep.richard.jgopherd;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.logging.ConsoleHandler;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;


public final class Main {
	public static Logger log;
	public static EProperties props;
	public static ServerListenThread listenthread;
	private static FileHandler fh;
	protected static String version = "0.1";
	
	public static void main(String[] args) {
		log = Logger.getGlobal();
		log.setLevel(Level.FINEST);
		log.setUseParentHandlers(false);
		ConsoleHandler ch = new ConsoleHandler();
		ch.setFormatter(new OneLineFormatter());
		ch.setLevel(Level.FINEST);
		log.addHandler(ch);
		log.info("Initializing server");
		try {
			fh = new FileHandler("jgopherd.log");
			fh.setFormatter(new OneLineFormatter());
			fh.setLevel(Level.FINEST);
			log.addHandler(fh);
		} catch (Throwable e) {
			log.warning("Failed to open log file: "+e.getMessage());
		}
		props = new EProperties();
		try {
			props.load(new FileInputStream("jgopherd.conf"));
		} catch (Throwable e) {
			log.warning("Creating new settings file due to failure to open file: "+e.getMessage());
			props.setPropertyString("name","127.0.0.1");
			props.setPropertyInt("port",70);
			props.setPropertyString("root","gopherdocs");
			props.setPropertyInt("timeout",15);
			try {
				props.store(new FileOutputStream("jgopherd.conf"),"jgopherd configuration file");
			} catch (Exception e1) {
				log.warning("Failed to save new settings: "+e.getMessage());
			}
		}
		listenthread = new ServerListenThread(props.getPropertyInt("port",70));
		listenthread.start();
		log.info("Now listening on port "+props.getPropertyInt("port",70));
		try {
			listenthread.join();
		} catch (Throwable e) {
			System.exit(1);
		}
		System.exit(0);
	}
}
