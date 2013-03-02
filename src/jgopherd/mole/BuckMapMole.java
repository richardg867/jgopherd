package jgopherd.mole;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.LinkedList;
import java.util.List;

import jgopherd.core.Main;
import jgopherd.gopher.GopherEntry;
import jgopherd.gopher.GopherRequest;
import jgopherd.gopher.GopherType;

public class BuckMapMole extends Mole {
	@Override
	public boolean canHandle(GopherRequest request) {
		return request.file.isDirectory() && new File(request.file, "gophermap").exists();
	}

	@Override
	public List<GopherEntry> handleRequest(GopherRequest request) throws Throwable {
		File map = new File(request.file, "gophermap");
		BufferedReader reader = new BufferedReader(new FileReader(map));
		
		List<GopherEntry> result = new LinkedList<GopherEntry>();
		
		String line;
		while ((line = reader.readLine()) != null) { // TODO log formatter, proper moles? test buck gophermap, live test
			String[] split = line.split("\t");
			if (split.length < 1) continue;
			else if (split.length == 1) {
				result.add(new GopherEntry(GopherType.Info, line));
				continue;
			}
			
			GopherType type = GopherType.fromType(split[0].charAt(0));
			String title = split[0].substring(1);
			
			String path = title;
			String server = Main.config.serverAddress;
			int port = Main.config.port;
			if (split.length > 1 && !split[1].trim().isEmpty()) {
				path = split[1].startsWith("/") ? split[1] : request.path + "/" + split[1];
				
				if (split.length > 2 && !split[2].trim().isEmpty()) {
					server = split[2];
					port = 70;
					
					if (split.length > 3 && !split[3].trim().isEmpty()) {
						try {
							port = Integer.parseInt(split[3]);
						} catch (NumberFormatException e) {}
					}
				}
			}
			
			result.add(new GopherEntry(type, title, path, server, port));
		}
		
		return result;
	}
}
