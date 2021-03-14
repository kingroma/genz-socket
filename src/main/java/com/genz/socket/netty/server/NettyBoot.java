package com.genz.socket.netty.server;

import javax.annotation.PostConstruct;

import org.springframework.stereotype.Component;

@Component
public class NettyBoot {
	
	@PostConstruct
    private void start() {
        new Thread(new Runnable() {

            @Override
            public void run() {
                try {
                    new NettySocketServer(5010).run();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

        }).start();
    }
}
