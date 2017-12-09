/**
 * 
 */
package com.fltck.aws.agent.tasks;

import java.text.SimpleDateFormat;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.AmqpException;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fltck.aws.agent.entity.HeartbeatMessage;

/**
 * @author brentlemons
 *
 */
@Component
public class MetadataTasks {

	private static final Logger logger = LoggerFactory.getLogger(MetadataTasks.class);
	
    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");

    @Value("${ami-id:N/A}")
    private String amiId;

    @Value("${hostname:N/A}")
    private String hostname;

    @Value("${instance-type:N/A}")
    private String instanceType;

    @Value("${services/domain:N/A}")
    private String serviceDomain;
    
	@Autowired
	private RabbitTemplate rabbitClient;


    @Scheduled(fixedDelay = 5000)
    public void reportCurrentTime() {
    	try {
	        logger.info("The time is now {}", dateFormat.format(new Date()));
	        logger.info("hostname: " + hostname);
	        ObjectMapper mapper = new ObjectMapper();
			mapper.registerModule(new JavaTimeModule());
			mapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
			rabbitClient.convertAndSend("fltck.aws", "heartbeat", mapper.writeValueAsString(new HeartbeatMessage(ZonedDateTime.now(ZoneOffset.UTC), hostname)));
		} catch (AmqpException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

    }

}
