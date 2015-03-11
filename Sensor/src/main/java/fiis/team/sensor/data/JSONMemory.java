package fiis.team.sensor.data;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class JSONMemory {
	public long total;
	public long ram;
	public long used;
	public long actualUsed;
}
