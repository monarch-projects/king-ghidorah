package org.titan.hermes.server;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import org.titan.hermes.common.codec.HermesMessageDecoder;
import org.titan.hermes.common.codec.HermesMessageEncode;
import org.titan.hermes.common.consts.HermesConst;

import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @Title: HermesServerBootStrap
 * @Description:
 * @Author: daozhang
 * @date: 2019/9/1
 */
public class HermesServerBootStrap {
    private EventLoopGroup boss = new NioEventLoopGroup(4, this.getFactory("netty-boss-event-loop-"));
    private EventLoopGroup worker = new NioEventLoopGroup(4, this.getFactory("netty-worker-event-loop-"));
    private ChannelHandler handler = new NettyServerHandler();


    private void bind(int... ports) {
        for (int port : ports) {

            Thread thread = new Thread(() -> {
                try {
                    ServerBootstrap bootstrap = new ServerBootstrap();
                    bootstrap.group(boss, worker)
                            .channel(NioServerSocketChannel.class)
                            .option(ChannelOption.SO_BACKLOG, 1024)
                            .childHandler(new ChannelInitializer<SocketChannel>() {
                                protected void initChannel(SocketChannel socketChannel) throws Exception {
                                    socketChannel.pipeline().addLast(new DelimiterBasedFrameDecoder(10000,
                                            PooledByteBufAllocator.DEFAULT.directBuffer().writeBytes(HermesConst.DELIMITER)));
                                    socketChannel.pipeline().addLast(new HermesMessageDecoder());
                                    socketChannel.pipeline().addLast(new HermesMessageEncode());
                                    socketChannel.pipeline().addLast(handler);
                                }
                            });
                    ChannelFuture future = bootstrap.bind(port).sync();
                    System.out.println("netty server started!");
                    future.channel().closeFuture().sync();
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    boss.shutdownGracefully();
                    worker.shutdownGracefully();
                }
            });

            thread.setName("boot-strap-" + port);
            thread.start();
        }

    }


    /**
     * build threadFactory
     *
     * @param prefix 线程名前缀
     * @return
     */
    private ThreadFactory getFactory(final String prefix) {

        return new ThreadFactory() {

            private AtomicInteger integer = new AtomicInteger(1);

            public Thread newThread(Runnable r) {
                Thread thread = new Thread(r);
                thread.setName(prefix + integer.getAndDecrement());
                return thread;
            }

        };
    }

    public static void main(String[] args) {
        new HermesServerBootStrap().bind(12306);
    }
}
