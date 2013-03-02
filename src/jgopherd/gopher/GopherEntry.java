package jgopherd.gopher;

import jgopherd.core.Main;

/**
 * A full gopher entry.
 * 
 * @author Richard
 */
public class GopherEntry {
	public GopherType type;
	public String title;
	public String path;
	public String server;
	public int port;
	
	public GopherEntry(GopherType type, String title, String path, String server, int port) {
		this.type = type;
		this.title = title.replaceAll("\t|\r|\n", " ");
		this.path = path;
		this.server = server;
		this.port = port;
	}
	
	public GopherEntry(GopherType type, String title, String path) {
		this(type, title, path, Main.config.serverAddress, Main.config.port);
	}
	
	public GopherEntry(GopherType type, String title) {
		this(type, title, "", Main.config.serverAddress, Main.config.port);
	}
	
	@Override
	public String toString() {
		return (type == null ? '0' : type.type) + title + "\t" + path + "\t" + server + "\t" + port;
	}
	
	public static GopherEntry parse(String line) {
		String[] split = line.split("\t");
		
		if (split[0].charAt(0) == '.') return null;
		
		String path = split[1];
		while (path.startsWith("/")) path = path.substring(1);
		
		return new GopherEntry(GopherType.fromType(split[0].charAt(0)), split[0].substring(1), path, split[2], Integer.parseInt(split[3]));
	}
}
