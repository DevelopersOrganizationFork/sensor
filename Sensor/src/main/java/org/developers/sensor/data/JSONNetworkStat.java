package org.developers.sensor.data;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class JSONNetworkStat {
	public long download;
	public long upload;
}
