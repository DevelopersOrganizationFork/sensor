package org.developers.sensor.source;
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
	
	private JMSConnection jmsConnnection = new JMSConnection();
	private ObjectMapper mapper = new ObjectMapper();
	
	public void run() {
		try {			
			JSONSystemInformation information = createSystemInformation();
			jmsConnnection.sendMessage(mapper.writeValueAsString(information));
			logger.info("information: " + mapper.writeValueAsString(information));
		} catch (SigarException e) {
			logger.error("Problem with reading paramiter in your computer.", e);
		} catch(JMSException e) {
			logger.error("Cannot send message to ActiveMQ");
			//TODO add reconnection reconnect
		} catch (JsonGenerationException | JsonMappingException e ) {
			logger.error("Problem with json ", e);
		} catch(Exception e) {
			logger.error("Problem with json ", e);
		}
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
		systemInformation.host = createHostInformation(sigar);
		systemInformation.name = measurement.network.mac;//TODO change to id or unique value
		
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

	private JSONHost createHostInformation(Sigar sigar) throws SigarException {
		JSONHost host = new JSONHost();
		host.hostname = sigar.getNetInfo().getHostName();
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

			for(String netInterface: sigar.getNetInterfaceList()) {
	            NetInterfaceStat netStat = sigar.getNetInterfaceStat(netInterface);
	            NetInterfaceConfig ifConfig = sigar.getNetInterfaceConfig(netInterface);
	            String hwaddr = ifConfig.getHwaddr();
	            String ip = sigar.getFQDN();
				if(ip.equals(ifConfig.getAddress())) {
	            	
	            	JSONNetworkStat stat = new JSONNetworkStat();

	    			long start = System.currentTimeMillis();
	    			long rxBytesStart = netStat.getRxBytes();
	    			long txBytesStart = netStat.getTxBytes();
	    			Thread.sleep(100);
	    			long end = System.currentTimeMillis();
	    			NetInterfaceStat statEnd = sigar.getNetInterfaceStat(netInterface);
	    			long rxBytesEnd = statEnd.getRxBytes();
	    			long txBytesEnd = statEnd.getTxBytes();

	    			stat.download = (rxBytesEnd - rxBytesStart)  / (end - start) * 100;
	    			stat.upload = (txBytesEnd - txBytesStart)  / (end - start) * 100;	            	
	            	
	            	logger.info("Download = " + stat.download + ", Upload = " + stat.upload);
	            	jsonNetwork.mac = hwaddr;
	            	jsonNetwork.ip = ip;	
	            	jsonNetwork.stat = stat;
	            }	
			}
		} catch (Exception e) {
			logger.error("Cannot create network statistics ", e);
		}
		
		return jsonNetwork;
	}
	
}
