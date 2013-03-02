package jgopherd.core;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.net.Socket;
import java.net.URLDecoder;
import java.util.List;
import java.util.logging.Level;

import jgopherd.gopher.GopherEntry;
import jgopherd.gopher.GopherRequest;
import jgopherd.mole.BuckMapMole;
import jgopherd.mole.DefaultMapMole;
import jgopherd.mole.ForkMole;
import jgopherd.mole.InternalMole;
import jgopherd.mole.JarMole;
import jgopherd.mole.Mole;

public class ClientThread implements Runnable {
	public static final Mole[] MOLES = new Mole[] {new InternalMole(), new JarMole(), new ForkMole()};
	public static final Mole[] MAP_MOLES = new Mole[] {new BuckMapMole(), new DefaultMapMole()};
	
	public final Socket socket;
	
	public BufferedReader reader;
	public PrintWriter writer;
	
	public ClientThread(Socket socket) {
		this.socket = socket;
	}
	
	@Override
	public void run() {
		GopherRequest request = request();
		Main.log.log(Level.INFO, request.code + " \"" + request.path + "\" [" + request.client.getHostName() + "/" + request.client.getHostAddress() + "]: " + request.params);
	}
	
	public GopherRequest request() {
		String line;
		OutputStream outputStream;
		
		try {
			socket.setSoTimeout(Main.config.requestTimeout);
			reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			writer = new PrintWriter(outputStream = socket.getOutputStream());
			
			line = reader.readLine();
		} catch (Throwable e) {
			return new GopherRequest("[unknown]", socket.getInetAddress(), socket.getPort(), null, 501);
		}
		
		while (line.startsWith("/")) line = line.substring(1);
		try {
			line = URLDecoder.decode(line, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			// oh please
		}
		
		GopherRequest request = new GopherRequest(line, socket.getInetAddress(), socket.getPort(), createFile(line));
		request.outputStream = outputStream;
		
		Mole mole = null;
		List<GopherEntry> entries = null;
		// TODO better way to resolve moles
		for (Mole lmole : MOLES) {
			if (lmole.canHandle(request)) {
				mole = lmole;
				break;
			}
		}
		
		// no mole could handle? serve file.
		if (mole == null) {
			if (!request.file.exists()) {
				request.code = 404;
				writer.println("3Resource not found\t/\terror.host\t70");
				writer.println("iFile does not exist.\t/\terror.host\t70");
				writer.println(".");
				
				closeSocket();
				return request;
			} else if (!request.file.canRead()) {
				request.code = 403;
				writer.println("3Access forbidden\t/\terror.host\t70");
				writer.println("iFile is not readable by the server.\t/\terror.host\t70");
				writer.println(".");
				
				closeSocket();
				return request;
			} else if (request.file.isDirectory()) {
				// TODO also need a better way to resolve map moles
				for (Mole lmole : MAP_MOLES) {
					if (lmole.canHandle(request)) {
						mole = lmole;
						break;
					}
				}
				
				if (mole == null) {
					writer.println("3Directory listing failed\t/\terror.host\t70");
					writer.println("iNo listing handler could list this directory.\t/\terror.host\t70");
					writer.println(".");
					
					closeSocket();
					return request;
				}
			} else { // serve file
				try {
					InputStream data = new FileInputStream(request.file);
					byte[] b = new byte[2048];
					int read;
					while ((read = data.read(b)) != -1) {
						request.outputStream.write(b, 0, read);
						request.outputStream.flush();
					}
					data.close();
				} catch (Throwable e) {}
			}
		}
		
		if (mole != null) {
			try {
				entries = mole.handleRequest(request);
			} catch (Throwable e) {
				request.code = 500;
				writer.println("3Internal error\t/\terror.host\t70");
				writer.println("iAn internal error has occurred while processing your request.\t/\terror.host\t70");
				writer.println("i\t/\terror.host\t70");
				
				StringWriter sw = new StringWriter();
				e.printStackTrace(new PrintWriter(sw));
				for (String stackLine : sw.toString().split("\n")) {
					writer.println("i" + stackLine.replace("\t", "    ") + "\t/\terror.host\t70");
				}
				writer.println(".");
				
				closeSocket();
				return request;
			}
		}
		
		if (entries != null) { // assume that if the mole returned null, it has assumed raw control of the connection
			for (GopherEntry entry : entries) {
				writer.println(entry.toString());
				writer.flush();
			}
			writer.println(".");
		}
		
		closeSocket();
		return request;
	}
	
	public File createFile(String dest) {
		String sanitized = dest.split("\\?")[0].replace("..", "");
		return new File(Main.config.documentRoot, sanitized);
	}
	
	public void closeSocket() {
		writer.flush();
		try {
			socket.close();
		} catch (IOException e) {}
	}
}
