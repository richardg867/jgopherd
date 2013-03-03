package jgopherd.gopher;

import java.io.File;
import java.io.OutputStream;
import java.net.InetAddress;

import jgopherd.core.Main;

/**
 * A gopher request made by a client.
 * 
 * @author Richard
 */
public class GopherRequest {
	public final String path;
	public final String params;
	public final File file;
	
	public final InetAddress client;
	public final int port;
	
	public OutputStream outputStream;
	public int code = 200;
	
	public GopherRequest(String path, InetAddress client, int port) {
		int idx = path.indexOf('?');
		if (idx == -1) idx = path.indexOf('\t');
		if (idx > -1) {
			this.path = path.substring(0, idx);
			this.params = path.substring(idx + 1);
		} else {
			this.path = path;
			this.params = "";
		}
		this.client = client;
		this.port = port;
		this.file = createFile();
	}
	
	public GopherRequest(String dest, InetAddress client, int port, int code) {
		this(dest, client, port);
		this.code = code;
	}
	
	public File createFile() {
		String sanitized = path.replace("..", "");
		return new File(Main.config.documentRoot, sanitized);
	}
}
