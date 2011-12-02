package com.viamep.richard.jgopherd;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.text.SimpleDateFormat;
import java.util.logging.Formatter;
import java.util.logging.Level;
import java.util.logging.LogRecord;

public class OneLineFormatter extends Formatter {

	@Override
	public String format(LogRecord lr) {
		StringBuilder s = new StringBuilder();
		s.append(new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(Long.valueOf(lr.getMillis()))+" ");
		Level lvl = lr.getLevel();
		if (lvl == Level.FINEST) s.append("[*] ");
		if (lvl == Level.FINER) s.append("[i] ");
		if (lvl == Level.FINE) s.append("[i] ");
		if (lvl == Level.INFO) s.append("[i] ");
		if (lvl == Level.WARNING) s.append("[!] ");
		if (lvl == Level.SEVERE) s.append("[X] ");
		s.append(lr.getMessage()+'\n');
		Throwable exc = lr.getThrown();
		if (exc != null) {
			StringWriter sw = new StringWriter();
			exc.printStackTrace(new PrintWriter(sw));
			s.append(sw.toString());
		}
		return s.toString();
	}

}
