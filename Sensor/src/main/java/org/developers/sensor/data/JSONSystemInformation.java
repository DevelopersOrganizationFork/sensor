package org.developers.sensor.data;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class JSONSystemInformation {
	public JSONHost host;
	public String name;
	public JSONMeasurement measurement;
	public Date date;
	
}
