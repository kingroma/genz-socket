package com.genz.socket.netty.server;

import java.net.InetSocketAddress;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;

public class NettySocketServer {
	private int port;

    public NettySocketServer(int port) {
        this.port = port;
    }

    // http://wonwoo.ml/index.php/post/536
    // https://github.com/Aiden7331/Netty-Chat/tree/java-chat-server-client/Netty_Chat_Server <<<<
    // 이걸로 함 해보자 
    public void run() {
    	//논블로킹 이벤트 루프 그룹 설정
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        try {
            ServerBootstrap b = new ServerBootstrap();
            b.group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .localAddress(new InetSocketAddress(port))
                    .childHandler(new NettyServerInitializer());
            ChannelFuture future = b.bind().sync();
            future.addListener(new ChannelFutureListener() {
                @Override
                public void operationComplete(ChannelFuture future) throws Exception {
                    if(future.isSuccess()){
                        System.out.println("Server bound");
                    }else{
                        System.out.println("Bound Attempt Failed");
                        future.cause().printStackTrace();
                    }
                }
            });
            /*
            채널의 closefuture를 얻을때까지
            현재 스래드 블로킹 상태 전환
            */
            future.channel().closeFuture().sync();
        } catch (Exception e) {
        	e.printStackTrace();
        } finally{
        	try {
        		bossGroup.shutdownGracefully().sync();
                workerGroup.shutdownGracefully().sync();
			} catch (Exception e2) {
				e2.printStackTrace();
			}
        }
    }
}
