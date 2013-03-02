package jgopherd.mole;

import java.net.URL;
import java.net.URLClassLoader;
import java.util.Enumeration;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import jgopherd.gopher.GopherEntry;
import jgopherd.gopher.GopherRequest;

/**
 * Wrapper for {@link Mole} types in jar files.
 * 
 * @author Richard
 */
public class JarMole extends Mole {
	@Override
	public boolean canHandle(GopherRequest request) {
		return request.file.exists() && request.file.getName().endsWith(".jar");
	}
	
	@Override
	public List<GopherEntry> handleRequest(GopherRequest request) throws Throwable {		
		JarFile jar = new JarFile(request.file);
		ClassLoader loader = URLClassLoader.newInstance(new URL[] {request.file.toURI().toURL()});
		
		Enumeration<JarEntry> entries = jar.entries();
		while (entries.hasMoreElements()) {
			JarEntry entry = entries.nextElement();
			String name = entry.getName();
			if (!name.endsWith(".class")) continue;
			
			try {
				Class clazz = Class.forName(name.substring(0, name.length() - 6).replace('/', '.'), false, loader);
				if (Mole.class.isAssignableFrom(clazz)) {
					Mole mole = (Mole) clazz.newInstance();
					return mole.handleRequest(request);
				}
			} catch (ClassNotFoundException e) {
				continue;
			}
		}
		
		return null;
	}
}
