package com.viamep.richard.jgopherd;

public class GopherEntry {
	public boolean raw = false;
	public char kind = '@';
	public String title = "";
	public String host = "";
	public int port = 65535;
	public String destination = "";
	
	public GopherEntry(char kind, String title, String host, int port, String destination) {
		this.kind = kind;
		this.title = title;
		this.host = host;
		this.port = port;
		this.destination = destination;
	}
	public GopherEntry(char kind) {
		this.kind = kind;
		this.title = "";
		this.host = Main.props.getPropertyString("name","127.0.0.1");
		this.port = Main.props.getPropertyInt("port",70);
		this.destination = "/";
	}
	public GopherEntry(char kind, String title) {
		this.kind = kind;
		this.title = title;
		this.host = Main.props.getPropertyString("name","127.0.0.1");
		this.port = Main.props.getPropertyInt("port",70);
		this.destination = "/";
	}
	public GopherEntry(char kind, String title, String host, int port) {
		this.kind = kind;
		this.title = title;
		this.host = host;
		this.port = port;
		this.destination = "/";
	}
	public GopherEntry(char kind, String title, String destination) {
		this.kind = kind;
		this.title = title;
		this.host = Main.props.getPropertyString("name","127.0.0.1");
		this.port = Main.props.getPropertyInt("port",70);
		this.destination = destination;
	}
	public GopherEntry(String text) {
		this.raw = true;
		this.title = text;
	}
	
	public String GetAsRaw() {
		if (raw) return title; 
		else return kind+title+'\t'+destination+'\t'+host+'\t'+port;
	}
}