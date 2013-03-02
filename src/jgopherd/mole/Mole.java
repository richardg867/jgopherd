package jgopherd.mole;

import java.util.List;

import jgopherd.gopher.GopherEntry;
import jgopherd.gopher.GopherRequest;

public abstract class Mole {
	public abstract boolean canHandle(GopherRequest request);
	
	public abstract List<GopherEntry> handleRequest(GopherRequest request) throws Throwable;
}
