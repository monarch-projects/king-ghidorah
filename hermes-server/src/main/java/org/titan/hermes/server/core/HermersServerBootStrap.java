package org.titan.hermes.server.core;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.channel.*;
import io.netty.channel.epoll.EpollChannelOption;
import io.netty.channel.epoll.EpollEventLoopGroup;
import io.netty.channel.epoll.EpollServerSocketChannel;
import io.netty.channel.kqueue.KQueueChannelOption;
import io.netty.channel.kqueue.KQueueEventLoopGroup;
import io.netty.channel.kqueue.KQueueServerSocketChannel;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;
import io.netty.util.concurrent.DefaultEventExecutorGroup;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.titan.hermes.common.codec.HermesMessageDecoder;
import org.titan.hermes.common.codec.HermesMessageEncode;
import org.titan.hermes.common.consts.HermesConst;
import org.titan.hermes.server.NettyServerHandler;

/**
 * @author starboyate
 */
public class HermersServerBootStrap {
	private final Logger logger = LoggerFactory.getLogger(HermersServerBootStrap.class);

	private EventLoopGroup bossGroup;

	private EventLoopGroup workerGroup;

	private DefaultEventExecutorGroup executors;

	private static final String OS_NAME = System.getProperty("os.name");

	private String host;

	private Integer port;

	public HermersServerBootStrap(String host, Integer port) {
		this.host = host;
		this.port = port;
	}

	public void start() throws Exception {
		ServerBootstrap bootstrap = new ServerBootstrap();
		group(bootstrap);
		this.executors = new DefaultEventExecutorGroup(50);
		bootstrap.childHandler(new ChannelInitializer<SocketChannel>() {
			protected void initChannel(SocketChannel socketChannel) throws Exception {
				socketChannel.pipeline().addLast(new DelimiterBasedFrameDecoder(10000,
						PooledByteBufAllocator.DEFAULT.directBuffer().writeBytes(HermesConst.DELIMITER)));
				socketChannel.pipeline().addLast(new HermesMessageDecoder());
				socketChannel.pipeline().addLast(new HermesMessageEncode());
				socketChannel.pipeline().addLast(executors, new NettyServerHandler());
			}
		});
		ChannelFuture channelFuture = bootstrap.bind(this.host, this.port).sync();
		channelFuture.addListener((ChannelFutureListener) future -> {
			if (future.cause() != null) {
				logger.error("hermes server start error:", future.cause());
			}
		}).channel().closeFuture().sync();
		logger.info("hermes started on port: " + port);
	}

	private void group(ServerBootstrap bootstrap) {
		this.executors = new DefaultEventExecutorGroup(Runtime.getRuntime().availableProcessors() * 2);
		if (OS_NAME.toLowerCase().contains("mac")) {
			this.bossGroup = new KQueueEventLoopGroup();
			this.workerGroup = new KQueueEventLoopGroup();
			bootstrap.channel(KQueueServerSocketChannel.class)
					.group(bossGroup, workerGroup);
		} else if (OS_NAME.toLowerCase().contains("linux")) {
			this.bossGroup = new EpollEventLoopGroup();
			this.workerGroup = new EpollEventLoopGroup();
			bootstrap.channel(EpollServerSocketChannel.class)
					.group(bossGroup, workerGroup)
					.option(EpollChannelOption.TCP_CORK, Boolean.TRUE);
		} else {
			this.workerGroup = new NioEventLoopGroup();
			this.bossGroup = new NioEventLoopGroup();
		}
		bootstrap.option(ChannelOption.TCP_NODELAY, Boolean.TRUE)
				.option(ChannelOption.SO_REUSEADDR, Boolean.TRUE)
				.option(KQueueChannelOption.SO_BACKLOG, 1024);
	}

	public void stop() {
		try {
			if (this.bossGroup != null) {
				this.bossGroup.shutdownGracefully().await();

			}
			if (this.workerGroup != null) {
				this.workerGroup.shutdownGracefully().await();
			}
			if (this.executors != null) {
				this.executors.shutdownGracefully().await();
			}
		} catch (Exception ex) {
			logger.error("hermes stop error: ", ex);
		}
	}
}
