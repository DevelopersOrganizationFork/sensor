import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import fiis.team.sensor.source.SensorRunnable;

/*
 * FIXME org.hyperic.sigar.SigarException: no sigar-amd64-winnt.dll in java.library.path
 * workaround - ustawiæ native library location: Sensor/libs
 * niestety w maven nie dziala
 * <configuration>
 * 	 <forkMode>once</forkMode>
 *   <argLine>-Djava.library.path=${basedir}/libs/</argLine>
 * </configuration>
 */

public class TestSigarApi {
	
	public static void main(String[] args) {
		
		ScheduledExecutorService executor = Executors
				.newSingleThreadScheduledExecutor();
		
		SensorRunnable runner = new SensorRunnable();
		
		executor.scheduleWithFixedDelay(runner, 0L, 5L, TimeUnit.SECONDS);
	}
}
