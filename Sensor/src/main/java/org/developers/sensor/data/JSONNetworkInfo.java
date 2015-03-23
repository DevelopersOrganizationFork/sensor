package org.developers.sensor.data;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class JSONNetworkInfo {
	public String mac;
	public String ip;
	public JSONNetworkStat stat;
}
