package org.developers.sensor.configuration;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class Context {
	

	private Map<String, String> parameters;

	public Context() {
		this.parameters = Collections.synchronizedMap(new HashMap<String, String>());
	}	
	
	public Context(Map<String, String> parameters) {
		this.parameters = Collections.synchronizedMap(parameters);
	}
	
	public Map<String, String> getParameters() {
		synchronized (parameters) {
			return new HashMap<String, String>(parameters);
		}
	}
	
	public void clear() {
		parameters.clear();
	}

	public void putAll(Map<String, String> map) {
		parameters.putAll(map);
	}

	public void put(String key, String value) {
		parameters.put(key, value);
	}
	
	private String get(String key) {
		return parameters.get(key);
	}
	
	public String getString(String key, String defaultValue) {
	    String value = get(key);
	    if(value != null) {
	      return value.trim();
	    }
	    return defaultValue;	
	}

	public long getLong(String key, long defaultValue) {
	    String value = get(key);
	    if(value != null) {
	      return Long.parseLong(value.trim());
	    }
	    return defaultValue;		
	}
	
	public int getInteger(String key, int defaultValue) {
	    String value = get(key);
	    if(value != null) {
	      return Integer.parseInt(value.trim());
	    }
	    return defaultValue;		
	}	
	
	public boolean getBoolean(String key, boolean defaultValue) {
	    String value = get(key);
	    if(value != null) {
	      return Boolean.parseBoolean(value.trim());
	    }
	    return defaultValue;		
	}	
	
}
