package org.developers.sensor.data;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class JSONMeasurement {

	public JSONMemory mem;
	public JSONCpu cpu;
	public JSONNetworkInfo network;
	public JSONDisk disk;
}
