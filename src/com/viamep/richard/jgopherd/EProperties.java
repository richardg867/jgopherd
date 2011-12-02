package com.viamep.richard.jgopherd;

import java.util.Properties;

public class EProperties extends Properties {
	private static final long serialVersionUID = 6418495828439021984L;

	public String getPropertyString(String key, String defaultValue) {
		try {
			return getProperty(key,defaultValue);
		} catch (Throwable e) {
			return defaultValue;
		}
	}
	
	public int getPropertyInt(String key, int defaultValue) {
		try {
			return Integer.parseInt(getProperty(key,Integer.toString(defaultValue)));
		} catch (Throwable e) {
			return defaultValue;
		}
	}
	
	public boolean getPropertyBoolean(String key, boolean defaultValue) {
		try {
			return Boolean.parseBoolean(getProperty(key,String.valueOf(defaultValue)));
		} catch (Throwable e) {
			return defaultValue;
		}
	}
	
	public Object setPropertyString(String key, String value) {
		return setProperty(key,value);
	}
	
	public Object setPropertyInt(String key, int value) {
		return setProperty(key,Integer.toString(value));
	}
	
	public Object setPropertyBoolean(String key, boolean value) {
		return setProperty(key,String.valueOf(value));
	}
}
