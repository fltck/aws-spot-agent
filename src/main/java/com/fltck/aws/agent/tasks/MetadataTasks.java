/**
 * 
 */
package com.fltck.aws.agent.tasks;

import java.time.ZoneOffset;
import java.time.ZonedDateTime;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.AmqpException;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.aws.core.env.ec2.AmazonEc2InstanceDataPropertySource;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.fltck.aws.agent.entity.HeartbeatMessage;

/**
 * @author brentlemons
 *
 */
@Component
public class MetadataTasks {
	private static final Logger logger = LoggerFactory.getLogger(MetadataTasks.class);
	
    private String amiId;
    private String hostname;
    private String instanceId;
    private String instanceType;
    private String serviceDomain;
    
	@Autowired
	private RabbitTemplate rabbitClient;
	
	private AmazonEc2InstanceDataPropertySource propertySource;
	
	public MetadataTasks() {
		propertySource = new AmazonEc2InstanceDataPropertySource("InstanceData");
		hostname = (String) propertySource.getProperty("hostname");
		amiId = (String) propertySource.getProperty("ami-id");
		instanceId = (String) propertySource.getProperty("instance-id");
		instanceType = (String) propertySource.getProperty("instance-type");
		serviceDomain = (String) propertySource.getProperty("services/domain");
	}

    @Scheduled(fixedDelay = 5000)
    public void checkInstanceAction() {
    	
    	String instanceAction = (String) propertySource.getProperty("spot/instance-action");
    	
    	if (instanceAction != null) {
	    	try {
		        logger.info("The time is now {}", ZonedDateTime.now(ZoneOffset.UTC));
		        logger.info("instanceAction: " + instanceAction);
				rabbitClient.convertAndSend("fltck.aws", "heartbeat", new HeartbeatMessage(ZonedDateTime.now(ZoneOffset.UTC), instanceId+"|"+instanceAction));
			} catch (AmqpException e) {
				logger.error(e.getMessage());
			}
    	}

    }

}
