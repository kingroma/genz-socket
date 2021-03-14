package com.genz.socket.netty.server;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.genz.socket.netty.command.NettyWhoAreUCommand;
import com.genz.socket.netty.message.NettyMessage;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.util.concurrent.GlobalEventExecutor;

@Sharable
public class NettySocketServerHandler extends ChannelInboundHandlerAdapter {
	protected static final Logger logger = LoggerFactory.getLogger(NettySocketServerHandler.class);
	
	private static final ChannelGroup channelGroup = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);
	
	// channelId , userId
	private static final Map<String,String> channelChannelIdAndUserIdMap = new HashMap<String,String>();
	private static final Map<String,Channel> channelUserIdMap = new HashMap<String,Channel>();

	private String whoareu = null ; 
	
	public void getChannelMap() {
		whoareu = new NettyWhoAreUCommand().commandObjectToString(); 
	}
	
    @Override
    public void channelRegistered(ChannelHandlerContext ctx) throws Exception{
    	System.out.println("channelRegistered");
        Channel newChannel = ctx.channel();
        // channel.write(whoareu);
        
        for(Channel channel : channelGroup){
            channel.write(
                    "[SERVER] ".concat(newChannel.remoteAddress().toString())
                            .concat(" login")
            );
        }
        channelGroup.add(newChannel);
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception{
    	System.out.println("channelActive");
        Channel channel = ctx.channel();
        logger.info("[New Client] remote address - ".concat(channel.remoteAddress().toString()));
        String channelId = channel.id().asLongText();
        channelGroup.add(channel);
        
        Channel newChannel = ctx.channel();
        System.out.println("[New Client] remote address - ".concat(newChannel.remoteAddress().toString()));
    }

    @Override
    public void channelUnregistered(ChannelHandlerContext ctx) throws Exception{
    	System.out.println("channelUnregistered");
        Channel channel = ctx.channel();
    }
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg)  throws Exception {
    	Channel channel = ctx.channel();
    	
    	System.out.println("channelRead " + msg);
    	
    	String message = null ; 
    	message = (String)msg ; 
    	
    	NettyMessage nm = null ; 
    	
    	if ( message != null ) {
    		nm = new NettyMessage(message);
    	}
    	
    	if ( nm != null && nm.getAction() != null) {
    		switch(nm.getAction()) {
    		case "iam" :
    			System.out.println(nm.getUserId() + "/" + channel.id().asLongText());
    			addChannel(nm.getUserId(),channel);
    			break;
    		case "pass" :
    			System.out.println("pass");
    			Channel toChannel = channelUserIdMap.get(nm.getToUserId());
    			System.out.println(toChannel);
    			if ( toChannel != null ) {
    				System.out.println(nm.getToUserId());
    				System.out.println(nm.getToMessage());
    				toChannel.write(nm.getToMessage());
    			}
    			
    			for(Channel c : channelGroup){
    				System.out.println(c.id().asLongText());
    	            c.write(nm.getToMessage());
    	        }
    			break;
    		}
    	}
    	
    	 for(Channel c : channelGroup){
             c.writeAndFlush(
                     "[".concat(channel.remoteAddress().toString())
                         .concat("]")
                         .concat(message)
                         .concat("\n")
             );
     }
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception{
    	System.out.println("channelReadComplete");
        ctx.flush();
    }
    
    public void addChannel(String userId, Channel channel) {
    	// channelChannelIdAndUserIdMap
    	// channelUserIdMap
    	channelChannelIdAndUserIdMap.put(channel.id().asLongText(), userId);
    	channelUserIdMap.put(userId, channel);
    }
}