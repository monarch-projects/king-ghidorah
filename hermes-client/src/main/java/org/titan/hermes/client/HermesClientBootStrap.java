package org.titan.hermes.client;


import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.retry.annotation.Retryable;
import org.titan.hermes.common.codec.HermesMessageDecoder;
import org.titan.hermes.common.codec.HermesMessageEncode;
import org.titan.hermes.common.consts.HermesConst;
import org.titan.hermes.common.exception.HermesException;
import org.titan.hermes.common.message.HermesMessage;

import javax.annotation.PreDestroy;
import java.util.Objects;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @Title: HermersClientBootStrap
 * @Description:
 * @Author: daozhang
 * @date: 2019/9/1
 */
@Slf4j
@Configuration
public class HermesClientBootStrap {
    private Channel channel;

    private EventLoopGroup group = new NioEventLoopGroup();

    private ExecutorService service;

    private AtomicInteger serviceId = new AtomicInteger(0);

    private HermesMessage heartBeat = new HermesMessage().setType(HermesMessage.HEART_BEAT);

    private HermesClientHandler handler = new HermesClientHandler();

    private String host;
    private int port;

    public void connect(String host, int port) {
        this.host = host;
        this.port = port;

        this.service = Executors.newFixedThreadPool(3, r ->
                new Thread(r, "hermes-client-boot-strap-" + serviceId.getAndIncrement())
        );

        CountDownLatch latch = new CountDownLatch(1);
        this.service.submit(() -> this.connect(host, port, latch));
        try {
            latch.await();
        } catch (InterruptedException e) {
            log.error("connect to hermes server occurs an exception", e);
            throw new HermesException("connect to hermes server occurs an exception");
        }

        this.channel.eventLoop().scheduleWithFixedDelay(() -> {

            if (Objects.nonNull(this.channel) && this.channel.isActive()) {
                this.channel.writeAndFlush(this.heartBeat);
                log.info("send heartbeat to hermes server");
            }

        }, 5, 3, TimeUnit.SECONDS);

        log.info("hermes client connect to hermes server successful!");
    }


    protected CountDownLatch connect() {

        CountDownLatch latch = new CountDownLatch(1);

        this.service.submit(() -> {
            this.connect(this.host, this.port, latch);
        });

        return latch;

    }

    @Retryable
    public void connect(String host, int port, CountDownLatch latch) {
        try {
            Bootstrap bootstrap = new Bootstrap();
            bootstrap.group(this.group)
                    .channel(NioSocketChannel.class)
                    .option(ChannelOption.SO_KEEPALIVE, true)
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel socketChannel) throws Exception {
                            socketChannel.pipeline().addLast(new DelimiterBasedFrameDecoder(10000,
                                    PooledByteBufAllocator.DEFAULT.directBuffer().writeBytes(HermesConst.DELIMITER)));
                            socketChannel.pipeline().addLast(new HermesMessageEncode());
                            socketChannel.pipeline().addLast(new HermesMessageDecoder());
                            socketChannel.pipeline().addLast(handler);
                        }
                    });

            ChannelFuture future = bootstrap.connect(host, port).sync();
            this.channel = future.channel();
            latch.countDown();
            future.channel().closeFuture().sync();
        } catch (Exception e) {
            log.error("error connect to hermes server ", e);
            throw new HermesException("error connect to hermes server");
        }
    }


    public Channel getChannel() {
        return this.channel;
    }

    @PreDestroy
    public void destroy() {
        this.service.shutdown();
        this.group.shutdownGracefully();
        this.channel.close();
    }

}
