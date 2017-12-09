/**
 * 
 */
package com.fltck.aws.agent.entity;

import java.time.ZonedDateTime;

import lombok.Data;

/**
 * @author brentlemons
 *
 */
@Data
public class HeartbeatMessage {
	
	private ZonedDateTime timestamp;
	private String hostname;

	/**
	 * @param timestamp
	 * @param hostname
	 */
	public HeartbeatMessage(ZonedDateTime timestamp, String hostname) {
		super();
		this.timestamp = timestamp;
		this.hostname = hostname;
	}

}
