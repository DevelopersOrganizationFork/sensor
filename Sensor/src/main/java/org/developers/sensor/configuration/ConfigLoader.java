package org.developers.sensor.configuration;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ConfigLoader {
	private static final Logger logger = LoggerFactory
			.getLogger(ConfigLoader.class);
	
	public static InputStream config;
	public static Properties properties = new Properties();
	
	static 
	{
		config = ConfigLoader.class.getResourceAsStream("client.properties");
		try {
			properties.load(config);
		} catch (IOException e) {
			logger.error("Cannot load config.", e);
		}
	}
	

}
