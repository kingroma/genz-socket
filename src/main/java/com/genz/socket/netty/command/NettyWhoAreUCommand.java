package com.genz.socket.netty.command;

public class NettyWhoAreUCommand extends NettyCommand {
	private static final long serialVersionUID = 1L ;
	
	public NettyWhoAreUCommand(){
		super.command = "whoareu";
	}
}
