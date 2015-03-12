package org.developers.sensor.source;
import org.developers.sensor.data.JSONCpu;
import org.developers.sensor.data.JSONMemory;
import org.developers.sensor.data.SystemInformation;
import org.hyperic.sigar.Cpu;
import org.hyperic.sigar.Mem;
import org.hyperic.sigar.Sigar;
import org.hyperic.sigar.SigarException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class SensorRunnable implements Runnable {

	private static final Logger logger = LoggerFactory
			.getLogger(SensorRunnable.class);
	
//	private Sender sender = new Sender(); TODO
	
	private void start() {
		// TODO Auto-generated method stub
		
	}
	
	public void run() {
		try {			
			SystemInformation information = createSystemInformation();
			
			//TODO send information to jms queue
		} catch (SigarException e) {
			logger.error("Problem with reading paramiter in your computer.", e);
		}
	}

	private SystemInformation createSystemInformation()
			throws SigarException {
		SystemInformation information = new SystemInformation();
		Sigar sigar = new Sigar();

		information.cpu = createCpuInformation(sigar.getCpu());
		information.mem = createMemoryInformation(sigar.getMem());
		information.hostname = sigar.getNetInfo().getHostName();
		
//		logger.info(String.valueOf(sigar.getNetgetMem().toString()));
//		logger.info(String.valueOf(sigar.getNetInfo().toString()));
//		logger.info(String.valueOf(sigar.getNetInfo().toString()));
//		logger.info(String.valueOf(sigar.getNetInterfaceConfig().toString()));
//		logger.info(String.valueOf(sigar.getThreadCpu().toString()));
//		logger.info(String.valueOf(sigar.getPid()));
//		logger.info(String.valueOf(sigar.getCpuPerc().toString()));
//		logger.info(String.valueOf(sigar.getFQDN().toString()));
//		logger.info(String.valueOf(sigar.getTcp().toString()));
//		logger.info(String.valueOf(sigar.getNetStat().toString()));
//		logger.info(String.valueOf(sigar.getResourceLimit().toString()));
//		logger.info(String.valueOf(sigar.getProcStat().toString()));
//		logger.info(String.valueOf(sigar.toString()));
//		logger.info(String.valueOf(sigar.getLoadAverage()));
		
		//TODO get list of partition
//		logger.info(String.valueOf(sigar.getDiskUsage("C:")));
//		logger.info(String.valueOf(sigar.getDiskUsage("D:")));
//		logger.info(String.valueOf(sigar.getDiskUsage("E:")));
		
		//TODO add netInformation
//		for(String netInterface: sigar.getNetInterfaceList()) {
//			logger.info(String.valueOf(sigar.getNetInterfaceStat(netInterface).getRxBytes()));
//		}
		
		return information;
	}

	private JSONMemory createMemoryInformation(Mem mem)
			throws SigarException {
		JSONMemory jsonMemory = new JSONMemory();
	
		jsonMemory.actualUsed = mem.getActualUsed();
		jsonMemory.ram = mem.getRam();
		jsonMemory.total = mem.getTotal();
		jsonMemory.used = mem.getUsed();
		
		logger.info(mem.toString());
		
		return jsonMemory;
	}

	private JSONCpu createCpuInformation(Cpu cpu)
			throws SigarException {
		JSONCpu jsonCpu = new JSONCpu();
		
		jsonCpu.irq = cpu.getIrq();
		jsonCpu.idle = cpu.getIdle();
		jsonCpu.nice = cpu.getNice();
		jsonCpu.softIrq = cpu.getSoftIrq();
		jsonCpu.stolen = cpu.getStolen();
		jsonCpu.sys = cpu.getSys();
		jsonCpu.total = cpu.getTotal();
		jsonCpu.user = cpu.getUser();
		jsonCpu.wait = cpu.getWait();
		
		logger.info(cpu.toString());
		
		return jsonCpu;
	}

	
	
}
