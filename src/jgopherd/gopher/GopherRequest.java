package jgopherd.gopher;

import java.io.File;
import java.io.OutputStream;
import java.net.InetAddress;

/**
 * A Gopher request made by a client.
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
	
	public GopherRequest(String dest, InetAddress client, int port, File file) {
		String[] split = dest.split("\\?");
		this.path = split[0];
		this.params = split.length < 2 ? "" : split[1];
		this.client = client;
		this.port = port;
		this.file = file;
	}
	
	public GopherRequest(String dest, InetAddress client, int port, File file, int code) {
		this(dest, client, port, file);
		this.code = code;
	}
}
