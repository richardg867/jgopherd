package jgopherd.mole;

import java.io.PrintWriter;
import java.util.LinkedList;
import java.util.List;

import jgopherd.core.Main;
import jgopherd.gopher.GopherEntry;
import jgopherd.gopher.GopherRequest;
import jgopherd.gopher.GopherType;

/**
 * Mole for the /_jgopherd documents.
 * 
 * @author Richard
 */
public class InternalMole extends Mole {
	@Override
	public boolean canHandle(GopherRequest request) {
		return request.path.startsWith("_jgopherd") || request.path.equals("caps.txt");
	}
	
	@Override
	public List<GopherEntry> handleRequest(GopherRequest request) throws Throwable {
		List<GopherEntry> result = new LinkedList<GopherEntry>();
		
		if (request.path.startsWith("_jgopherd")) {
			result.add(new GopherEntry(GopherType.Info, "This is jgopherd " + Main.VERSION));
			result.add(new GopherEntry(GopherType.Info, ""));
			result.add(new GopherEntry(GopherType.Info, "JVM: " + System.getProperty("java.vm.vendor") + " version " + System.getProperty("java.runtime.version")));
			result.add(new GopherEntry(GopherType.Info, "OS: " + System.getProperty("os.name") + " version " + System.getProperty("os.version") + " (" + System.getProperty("os.arch") + ")"));
			Runtime rt = Runtime.getRuntime();
			result.add(new GopherEntry(GopherType.Info, "Memory usage: " + ((rt.totalMemory() - rt.freeMemory()) / 1024 / 1024) + " MB used, " + (rt.totalMemory() / 1024 / 1024) + " MB allocated, " + (rt.maxMemory() / 1024 / 1024) + " MB total"));
		} else if (request.path.equals("caps.txt")) {
			PrintWriter writer = new PrintWriter(request.outputStream);
			writer.println("CAPS");
			writer.println("CapsVersion=1");
			writer.println("ExpireCapsAfter=3600");
			writer.println("PathDelimeter=/");
			writer.println("PathIdentity=.");
			writer.println("PathParent=..");
			writer.println("PathParentDouble=FALSE");
			writer.println("PathKeepPreDelimeter=FALSE");
			writer.println("ServerSoftware=jgopherd");
			writer.println("ServerSoftwareVersion=" + Main.VERSION);
			writer.println("ServerArchitecture=" + System.getProperty("os.name") + " (" + System.getProperty("os.arch") + ")");
			writer.println("ServerDescription=Java Virtual Machine: " + System.getProperty("java.vm.vendor") + " version " + System.getProperty("java.runtime.version"));
			writer.println("ServerSupportsStdinScripts=FALSE");
			writer.flush();
			
			return null;
		}
		
		return result;
	}
}
