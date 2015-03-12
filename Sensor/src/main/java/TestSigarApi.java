
import org.developers.sensor.client.SensorClient;

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
		new SensorClient().start();
	}
}
