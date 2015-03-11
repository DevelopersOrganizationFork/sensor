package fiis.team.sensor.data;

import com.fasterxml.jackson.annotation.JsonInclude;


@JsonInclude(JsonInclude.Include.NON_NULL)
public class JSONCpu {
	public long user;
	public long sys;
	public long nice;
	public long idle;
	public long wait;
	public long irq;
	public long softIrq;
	public long stolen;
	public long total;	
}
