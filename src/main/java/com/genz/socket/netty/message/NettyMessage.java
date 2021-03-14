package com.genz.socket.netty.message;

import java.io.Serializable;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.genz.socket.netty.command.NettyCommand;

public class NettyMessage implements Serializable {
	private static final long serialVersionUID = 1L ;
	
	protected static final Logger logger = LoggerFactory.getLogger(NettyCommand.class);
	
	protected static final ObjectMapper om = new ObjectMapper();
	
	private String userId ; 
	
	private String action ; 
	
	private String message ;
	
	private String toUserId ; 
	
	private String toMessage ; 

	@SuppressWarnings("unchecked")
	public NettyMessage(String data) {
		try {
			Map<String,String> map = om.readValue(data, Map.class);
			this.userId = map.get("userId");
			this.action = map.get("action");
			this.message = map.get("message");
			this.toUserId = map.get("toUserId");
			this.toMessage = map.get("toMessage");
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getAction() {
		return action;
	}

	public void setAction(String action) {
		this.action = action;
	}

	public String getToUserId() {
		return toUserId;
	}

	public void setToUserId(String toUserId) {
		this.toUserId = toUserId;
	}

	public String getToMessage() {
		return toMessage;
	}

	public void setToMessage(String toMessage) {
		this.toMessage = toMessage;
	}
}
