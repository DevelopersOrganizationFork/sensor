package org.developers.sensor.data;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class JSONHost {
	public String hostname;
	public String ip;
}
