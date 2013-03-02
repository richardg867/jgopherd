package jgopherd.core;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Formatter;
import java.util.logging.Level;
import java.util.logging.LogRecord;

public class LogFormatter extends Formatter {
	public static final Map<Level, String> LEVELS = new HashMap<Level, String>(9);
	public static final DateFormat FORMAT = new SimpleDateFormat();
	
	@Override
	public String format(LogRecord arg0) {
		return "[" + FORMAT.format(new Date(arg0.getMillis())) + "] [" + LEVELS.get(arg0.getLevel()) + "] " + arg0.getMessage() + "\n";
	}
	
	static {
		LEVELS.put(Level.FINE, "$");
		LEVELS.put(Level.FINER, "#");
		LEVELS.put(Level.INFO, "*");
		LEVELS.put(Level.WARNING, "!");
		LEVELS.put(Level.SEVERE, "!!!");
		
		LEVELS.put(Level.OFF, "");
		LEVELS.put(Level.ALL, "");
		LEVELS.put(Level.CONFIG, ">");
	}
}
