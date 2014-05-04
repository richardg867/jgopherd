package jgopherd.core;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Field;
import java.util.Properties;

public class Configuration extends Properties {
	private static final long serialVersionUID = -8374423544106243038L;
	
	@Property
	public String serverAddress = "127.0.0.1";
	@Property
	public String documentRoot = "gopherdocs";
	@Property
	public int port = 70;
	@Property
	public int requestTimeout = 15000;
	@Property
	public boolean printRequests = true;
	@Property
	public boolean executeSupported = Main.executeSupported;
	@Property
	public String moleRegex = ".+\\.mol$";
	@Property
	public boolean exposeStats = true;
	@Property
	public boolean exposeCaps = true;
	
	public Configuration() {
		super();
	}
	
	public void loadFromProperties() {
		try {
			for (Field field : getClass().getDeclaredFields()) {
				Property prop = field.getAnnotation(Property.class);
				if (prop != null) {
					String name = prop.name();
					String value = getProperty(name.isEmpty() ? field.getName() : name, null);
					if (value == null) continue;
					
					if (field.getType() == String.class) {
						field.set(this, value);
					} else if (field.getType() == Integer.class) {
						try {
							field.setInt(this, Integer.parseInt(value));
						} catch (NumberFormatException e) {
							continue;
						}
					}
				}
			}
		} catch (Throwable e) {
			e.printStackTrace();
		}
	}
	
	public void saveToProperties() {
		try {
			for (Field field : getClass().getDeclaredFields()) {
				Property prop = field.getAnnotation(Property.class);
				if (prop != null) {
					String name = prop.name();
					setProperty(name.isEmpty() ? field.getName() : name, field.get(this).toString());
				}
			}
		} catch (Throwable e) {
			e.printStackTrace();
		}
	}
	
	@Retention(RetentionPolicy.RUNTIME)
	@Target(value = {ElementType.FIELD})
	public static @interface Property {
		public String name() default "";
	}
}
