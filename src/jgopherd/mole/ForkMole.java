package jgopherd.mole;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import jgopherd.core.Main;
import jgopherd.gopher.GopherEntry;
import jgopherd.gopher.GopherRequest;

/**
 * Old-fashioned process mole.
 * 
 * @author Richard
 */
public class ForkMole extends Mole {
	@Override
	public boolean canHandle(GopherRequest request) {
		return request.file.exists() && (Main.executeSupported ? request.file.canExecute() : request.file.getName().endsWith(".mol"));
	}
	
	@Override
	public List<GopherEntry> handleRequest(GopherRequest request) throws Throwable {
		ProcessBuilder builder = new ProcessBuilder();
		List<String> params = new ArrayList<String>();
		params.add(request.file.getAbsolutePath());
		params.addAll(Arrays.asList(request.params.split(" ")));
		builder.command(params);
		
		// define CGI variables
		Map<String, String> env = builder.environment();
		env.put("REMOTE_HOST", request.client.getHostName());
		env.put("REMOTE_ADDR", request.client.getHostAddress());
		env.put("REMOTE_PORT", "" + request.port);
		env.put("SERVER_HOST", Main.config.serverAddress);
		env.put("SERVER_PORT", "" + Main.config.port);
		env.put("SELECTOR", request.path + (request.params.isEmpty() ? "" : "?" + request.params));
		env.put("REQUEST", request.path);
		
		Process process = builder.start();
		
		InputStream data = process.getInputStream();
		byte[] b = new byte[2048];
		int read;
		while ((read = data.read(b)) != -1) {
			request.outputStream.write(b, 0, read);
			request.outputStream.flush();
		}
		
		return null;
	}
}
