package jgopherd.mole;

import java.io.File;
import java.io.FilenameFilter;
import java.util.LinkedList;
import java.util.List;

import jgopherd.gopher.GopherEntry;
import jgopherd.gopher.GopherRequest;
import jgopherd.gopher.GopherType;

public class DefaultMapMole extends Mole {
	@Override
	public boolean canHandle(GopherRequest request) {
		return request.file.isDirectory();
	}

	@Override
	public List<GopherEntry> handleRequest(GopherRequest request) throws Throwable {
		List<GopherEntry> result = new LinkedList<GopherEntry>();
		/*result.add(new GopherEntry(GopherType.Info, "Index of " + request.path + (request.path.endsWith("/") ? "" : "/")));
		result.add(new GopherEntry(GopherType.Info, ""));*/
		
		for (File file : request.file.listFiles(new FilenameFilter() {
			@Override
			public boolean accept(File arg0, String arg1) {
				return !arg0.isHidden();
			}
		})) {
			result.add(new GopherEntry(file.isDirectory() ? GopherType.Menu : GopherType.fromExtension(file.getName()), file.getName(), request.path + "/" + file.getName()));
		}
		
		return result;
	}
}
