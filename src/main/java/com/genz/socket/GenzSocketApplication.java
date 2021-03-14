package com.genz.socket;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.genz.socket.netty.command.NettyCommand;
import com.genz.socket.netty.command.NettyWhoAreUCommand;

@SpringBootApplication
public class GenzSocketApplication {

	public static void main(String[] args) {
		SpringApplication.run(GenzSocketApplication.class, args);
	}
	// {"userId":"123","action":"iam","message":"message"}
	// {"userId":"234","action":"iam","message":"message"}
	// {"userId":"123","action":"pass","toUserId":"234","toMessage":"message","message":"message"}
}
