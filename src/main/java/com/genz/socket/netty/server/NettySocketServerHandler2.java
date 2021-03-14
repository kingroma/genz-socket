package com.genz.socket.netty.server;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelId;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.util.concurrent.GlobalEventExecutor;

@Sharable
public class NettySocketServerHandler2 extends ChannelInboundHandlerAdapter {
	protected static final Logger logger = LoggerFactory.getLogger(NettySocketServerHandler2.class);
	
//	private static final ChannelGroup channelGroup = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);
	
	// channelId , userId
	private static final Map<ChannelId,String> channelIdUserIdMap = new HashMap<ChannelId,String>();
	private static final Map<String,Channel> userIdChannelMap = new HashMap<String,Channel>();
	
    @Override
    public void channelRegistered(ChannelHandlerContext ctx) throws Exception{
    	// Channel openChannel = ctx.channel();
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception{
    	
    }

    @Override
    public void channelUnregistered(ChannelHandlerContext ctx) throws Exception{
    	Channel closeChannel = ctx.channel() ;
    	
    	ChannelId channelId = closeChannel.id();
    	String userId = channelIdUserIdMap.get(channelId);
    	
    	if ( userId != null ) {
    		userIdChannelMap.remove(userId);
    		channelIdUserIdMap.remove(channelId);
    		
    		logger.info("channelUnregistered " + userId + " / " + channelId );
    	}
    }
    
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg)  throws Exception {
    	Channel channel = ctx.channel();
    	
    	String message = (String) msg ;
    	ChannelId channelId = channel.id();
    	
    	if ( message != null && !message.isEmpty() ) {
    		if ( message.startsWith("login") ) {
    			String[] split = message.split(" ");
    			
    			if ( split.length >= 2 ) {
					String userId = split[1];
					
					Channel tempChannel = userIdChannelMap.get(userId);
					if ( tempChannel != null && tempChannel.isOpen() ) {
						tempChannel.close();
					}
					
					String tempUserId = channelIdUserIdMap.get(channelId);
					if ( tempUserId != null ) {
						Channel tempChannel2 = userIdChannelMap.get(tempUserId);
						if ( tempChannel2 != null && tempChannel2.isOpen() ) {
							tempChannel2.close();
						} 
					}
					
					channelIdUserIdMap.put(channelId, userId);
					userIdChannelMap.put(userId, channel);
					
					logger.info("login success userId = {}, command = {} ", userId, message);
					channel.write("200");
    			} else {
    				channel.write("300");
    			}
    		}
    		
    		else if ( message.startsWith("send") ) {
    			String[] split = message.split(" ");
    			
    			if ( split.length >= 3 ) {
    				String toUserId = split[1];
        			String data = message.substring("send".length() + 1 + toUserId.length() + 1, message.length());
        			
        			Channel toChannel = userIdChannelMap.get(toUserId);
        			String fromUserId = channelIdUserIdMap.get(channelId);
        			
        			if ( toChannel != null && toChannel.isActive() ) {
    	    			toChannel.writeAndFlush(data);
    	    			logger.info("send success fromUserId = {}, command = {} ", fromUserId, message );
    	    			channel.write("200");
        			} else {
        				channel.write("400");
        			}
    			} else {
    				channel.write("300");
    			}
    			
    			
    		} else {
    			channel.write("404");
    		}
    	}
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception{
        ctx.flush();
    }
    
}