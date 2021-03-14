package com.genz.socket.netty.command;

import java.io.Serializable;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.ObjectMapper;

public class NettyCommand implements Serializable {
	private static final long serialVersionUID = 1L ;
	
	protected static final Logger logger = LoggerFactory.getLogger(NettyCommand.class);
	
	protected static final ObjectMapper om = new ObjectMapper(); 
	
	protected String command ; 

	public String getCommand() {
		return command;
	}

	public void setCommand(String command) {
		this.command = command;
	};
	
	public String commandObjectToString() {
		String result = null ; 
		try {
			result = om.writeValueAsString(this);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("commandObjectToString error");
		}
		return result; 
	}
}
