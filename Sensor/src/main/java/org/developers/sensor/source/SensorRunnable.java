package org.developers.sensor.source;
import java.util.Date;

import javax.jms.JMSException;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.developers.sensor.data.JSONCpu;
import org.developers.sensor.data.JSONDisk;
import org.developers.sensor.data.JSONHost;
import org.developers.sensor.data.JSONMeasurement;
import org.developers.sensor.data.JSONMemory;
import org.developers.sensor.data.JSONNetworkInfo;
import org.developers.sensor.data.JSONNetworkStat;
import org.developers.sensor.data.JSONSystemInformation;
import org.developers.sensor.jms.producer.JMSConnection;
import org.hyperic.sigar.Cpu;
import org.hyperic.sigar.Mem;
import org.hyperic.sigar.NetInterfaceConfig;
import org.hyperic.sigar.NetInterfaceStat;
import org.hyperic.sigar.Sigar;
import org.hyperic.sigar.SigarException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class SensorRunnable implements Runnable {

	private static final Logger logger = LoggerFactory
			.getLogger(SensorRunnable.class);
	private static final String INACTIVE_IP = "0.0.0.0";
	
	private JMSConnection jmsConnnection = new JMSConnection();
	private ObjectMapper mapper = new ObjectMapper();
	
	public void run() {
		try {			
			JSONSystemInformation information = createSystemInformation();
			jmsConnnection.sendMessage(mapper.writeValueAsString(information));
			logger.info("information: " + mapper.writeValueAsString(information));
		} catch (SigarException e) {
			logger.error("Problem with reading paramiter in your computer.", e);
			reconnect();
		} catch(JMSException e) {
			logger.error("Cannot send message to ActiveMQ");
			reconnect();
		} catch (JsonGenerationException | JsonMappingException e ) {
			reconnect();
			logger.error("Problem with json ", e);
		} catch(Exception e) {
			reconnect();
			logger.error("Unknown exception ", e);
		}
	}

	private void reconnect() {
		logger.info("Try to reconnect");
		jmsConnnection = new JMSConnection();
	}

	private JSONSystemInformation createSystemInformation()
			throws SigarException {
		JSONSystemInformation systemInformation = new JSONSystemInformation();
		JSONMeasurement measurement = new JSONMeasurement();
		Sigar sigar = new Sigar();

		measurement.cpu = createCpuInformation(sigar.getCpu());
		measurement.mem = createMemoryInformation(sigar.getMem());
		measurement.network = createNetworkInformation(sigar);
		measurement.disk = createDiskInformation();//TODO
		
		systemInformation.measurement = measurement;
		systemInformation.name = measurement.network.mac;//TODO change to id or unique value
		systemInformation.host = createHostInformation(sigar, systemInformation.name);
		systemInformation.date = new Date();
		
//		logger.info(String.valueOf(sigar.getDiskUsage("C:")));
//		logger.info(String.valueOf(sigar.getDiskUsage("D:")));
//		logger.info(String.valueOf(sigar.getDiskUsage("E:")));
		
		return systemInformation;
	}

	private JSONDisk createDiskInformation() {
		JSONDisk disk = new JSONDisk();
		disk.read = 0;
		disk.write = 0;
		return disk;
	}

	private JSONHost createHostInformation(Sigar sigar, String name) throws SigarException {
		JSONHost host = new JSONHost();
		host.hostname = sigar.getNetInfo().getHostName() + "_" + name;
		host.ip = sigar.getNetInfo().getPrimaryDns();
		return host;
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

	private JSONNetworkInfo createNetworkInformation(Sigar sigar) {
		JSONNetworkInfo jsonNetwork = new JSONNetworkInfo();

		
		try {
			JSONNetworkStat stat = new JSONNetworkStat();
			stat.download = 0;
			stat.upload = 0;
			String ip = sigar.getFQDN();
			jsonNetwork.stat = stat;
			
			
			for(String netInterface: sigar.getNetInterfaceList()) {
	            NetInterfaceStat netStat = sigar.getNetInterfaceStat(netInterface);
	            NetInterfaceConfig ifConfig = sigar.getNetInterfaceConfig(netInterface);
	            String hwaddr = ifConfig.getHwaddr();

	    			long start = System.currentTimeMillis();
	    			long rxBytesStart = netStat.getRxBytes();
	    			long txBytesStart = netStat.getTxBytes();
	    			Thread.sleep(100);
	    			long end = System.currentTimeMillis();
	    			NetInterfaceStat statEnd = sigar.getNetInterfaceStat(netInterface);
	    			long rxBytesEnd = statEnd.getRxBytes();
	    			long txBytesEnd = statEnd.getTxBytes();
            		jsonNetwork.mac = hwaddr;
            		jsonNetwork.ip = ip;	
	    			stat.download = (rxBytesEnd - rxBytesStart)  / (end - start) * 100;
	    			stat.upload = (txBytesEnd - txBytesStart)  / (end - start) * 100;	            	
	            	if(stat.download == 0 && stat.upload == 0) {
	            		continue;
	            	} else {
	            		logger.info("Download = " + stat.download + ", Upload = " + stat.upload);
	            		jsonNetwork.stat = stat;
	            		break;
	            	}
			}
		} catch (Exception e) {
			logger.error("Cannot create network statistics ", e);
		}
		
		return jsonNetwork;
	}
	
}
