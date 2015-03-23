package org.developers.sensor.jms.producer;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.JMSException;
import javax.jms.MessageProducer;
import javax.jms.Queue;
import javax.jms.Session;
import javax.jms.TextMessage;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.developers.sensor.configuration.ConfigLoader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JMSConnection {
	
	private static final Logger logger = LoggerFactory
			.getLogger(JMSConnection.class);

	private static final String DEFAULT_USERNAME = "admin";
	private static final String DEFAULT_PASSWORD = "admin";
	private static final String DEFAULT_URL = "tcp://localhost:61616";
	private static final String DEFAULT_QUEUE_NAME = "queue";
	private static final String USERNAME = "JMSUsername";
	private static final String PASSWORD = "JMSPassword";
	private static final String URL = "JMSURL";
	private static final String QUEUE_NAME = "JMSQueueName";
	
	private MessageProducer producer;
	private TextMessage msg;
	
	public JMSConnection() {
		initConnection();		
	}
	
	public void initConnection() {
		
		final String username = ConfigLoader.properties.getProperty(USERNAME, DEFAULT_USERNAME);
		final String password = ConfigLoader.properties.getProperty(PASSWORD, DEFAULT_PASSWORD);
		final String url = ConfigLoader.properties.getProperty(URL, DEFAULT_URL);
		final String queueName = ConfigLoader.properties.getProperty(QUEUE_NAME, DEFAULT_QUEUE_NAME);
		
		ConnectionFactory factory = new ActiveMQConnectionFactory(username,password,url);
		try {
			Connection connection = factory.createConnection();
			Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
			Queue queue = session.createQueue(queueName);
			producer = session.createProducer(queue);
			msg = session.createTextMessage();
			
			logger.info("Connection created ");
		} catch (JMSException e) {
			logger.error("Exception during create connection",e);
		}
		
	}
	
	public void sendMessage(String message) throws JMSException {
		msg.setText(message);
		producer.send(msg);		
	}
	
	public void reconnect() {
		throw new NoClassDefFoundError("TODO mehod not implemented yet");
	}
	
}
