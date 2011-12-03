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
	private static boolean quiet = false;
	private static String proxy = null;
	private static String proxy2 = null;
	private static String pip = "0.0.0.0";
	@SuppressWarnings("unused")
	private static String phost = "0.0.0.0";
	private static int pport = 65535;
	protected static String version = "0.1";
	
	public static void main(String[] args) {
		pip = Util.GetEnv("REMOTE_ADDR",pip);
		phost = Util.GetEnv("REMOTE_HOST",pip);
		try {
			pport = Integer.parseInt(Util.GetEnv("REMOTE_PORT",pip));
		} catch (Throwable e) {
			// do nothing
		}
		for (String arg : args) {
			if (arg.startsWith("-proxy=")) {
				quiet = true;
				int i = 0;
				String argx = arg.substring(7);
				proxy = "";
				while (true) {
					try {
						proxy += (char)Integer.parseInt(argx.charAt(i)+""+argx.charAt(i+1),16);
					} catch (Throwable e) {
						break;
					}
					i++;
					i++;
				}
				proxy2 = argx;
				if (((proxy2.length() % 2) != 0) || (proxy.length() == 0)) {
					proxy = "\1INVALID\1";
				}
			} else if (arg.startsWith("-proxyip=")) {
				pip = arg.substring(9);
			} else if (arg.startsWith("-proxyhost=")) {
				phost = arg.substring(11);
			} else if (arg.startsWith("-proxyport=")) {
				try {
					pport = Integer.parseInt(arg.substring(11));
				} catch (Throwable e) {
					// do nothing
				}
			}
		}
		log = Logger.getGlobal();
		log.setLevel(Level.FINEST);
		log.setUseParentHandlers(false);
		if (!quiet) {
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
			} catch	(Throwable e) {
				log.warning("Failed to open log file: "+e.getMessage());
			}
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
				log.info("New settings created");
			} catch (Exception e1) {
				log.warning("Failed to save new settings: "+e.getMessage());
			}
		}
		if ((proxy != null) && (proxy != "") && !proxy.startsWith("\1")) {
			String[] psplit = proxy.split("\\?");
			String line = "/";
			String params = "";
			try {
				line = psplit[0];
			} catch (Throwable e) {
				line = "/";
			}
			try {
				params = psplit[1];
			} catch (Throwable e) {
				params = "";
			}
			for (GopherEntry ge : new ClientThread(pip,pport).MakeEntries(line,params,false)) System.out.println(ge.GetAsRaw());
		} else if (proxy == "\1INVALID\1") {
			String[] sa = {"An invalid (either empty or not properly hexpacked) proxy request has been issued.","Original request: "+proxy2};
			for (GopherEntry ge : new ClientThread(pip,pport).MakeError("Invalid proxy request",sa)) System.out.println(ge.GetAsRaw());
		} else {
			listenthread = new ServerListenThread(props.getPropertyInt("port",70));
			listenthread.start();
			log.info("Now listening on port "+props.getPropertyInt("port",70));
			try {
				listenthread.join();
			} catch (Throwable e) {
				System.exit(1);
			}
		}
		System.exit(0);
	}
}