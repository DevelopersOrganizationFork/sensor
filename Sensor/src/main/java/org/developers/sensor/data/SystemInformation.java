package org.developers.sensor.data;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class SystemInformation {

	public JSONMemory mem;
	public JSONCpu cpu;
	public JSONNetwork network;
	public String hostname;
}
