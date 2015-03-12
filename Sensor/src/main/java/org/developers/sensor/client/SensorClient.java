package org.developers.sensor.client;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.developers.sensor.configuration.ConfigLoader;
import org.developers.sensor.configuration.Context;
import org.developers.sensor.source.SensorRunnable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SensorClient {
	
	private static final long DEFAULT_DELAY = 0L;
	private static final long DEFAULT_INTERVAL = 5L;

	private static final String INTERVAL = "Schedule";
	private static final String DELAY = "Delay";

	private static final Logger logger = LoggerFactory
			.getLogger(SensorClient.class);
	
	private Context context = new Context();
	
	public SensorClient() {
		init();
	}

	private void init() {
		context.put(INTERVAL, ConfigLoader.properties.getProperty(INTERVAL));
		context.put(DELAY, ConfigLoader.properties.getProperty(DELAY));
		
		logger.info(ConfigLoader.properties.getProperty(INTERVAL));
		logger.info(ConfigLoader.properties.getProperty(DELAY));
	}
	
	public void start() {
		ScheduledExecutorService executor = Executors
				.newSingleThreadScheduledExecutor();
		
		SensorRunnable runner = new SensorRunnable();
		
		executor.scheduleWithFixedDelay(runner,
				context.getLong(DELAY, DEFAULT_DELAY), 
				context.getLong(INTERVAL, DEFAULT_INTERVAL), 
				TimeUnit.SECONDS);
	}
}
