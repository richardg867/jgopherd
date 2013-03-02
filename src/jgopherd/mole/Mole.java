package jgopherd.mole;

import java.util.List;

import jgopherd.gopher.GopherEntry;
import jgopherd.gopher.GopherRequest;

public abstract class Mole {
	/**
	 * Check whether this mole can handle the specified request.
	 * 
	 * @param request Request
	 * @return Whether the mole can handle the request
	 */
	public abstract boolean canHandle(GopherRequest request);
	
	/**
	 * Handle the specified request.
	 * 
	 * @param request The request
	 * @return List of {@link GopherEntry}, or null if the mole assumed raw control of the connection
	 */
	public abstract List<GopherEntry> handleRequest(GopherRequest request) throws Throwable;
}
