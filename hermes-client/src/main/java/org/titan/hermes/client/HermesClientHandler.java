package org.titan.hermes.client;

import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.titan.hermes.client.listener.HermesListerContainer;
import org.titan.hermes.client.network.MessageContainer;
import org.titan.hermes.common.message.HermesMessage;

import java.util.concurrent.CountDownLatch;

/**
 * @Title: HermesClientHandler
 * @Description:
 * @Author: daozhang
 * @date: 2019/9/1
 */
@Slf4j
@Configuration
@ChannelHandler.Sharable
public class HermesClientHandler extends SimpleChannelInboundHandler<HermesMessage> {
    @Autowired
    private HermesListerContainer container;
    @Autowired
    private MessageContainer messageContainer;
    @Autowired
    private HermesClientBootStrap strap;

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, HermesMessage msg) throws Exception {
        if (msg.getType() == HermesMessage.USER)
            container.excuterMessage(msg);
        else if (msg.getType() == HermesMessage.SERVER_EXCHANGE_BINDINGS)
            this.container.initServerBindings(msg);
        else
            this.messageContainer.receive(msg);
    }


    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        log.info("channel inactive,go to reconnect");
        CountDownLatch latch = this.strap.connect();
        latch.await();
        log.info("reconnect to hermes server");
        super.channelInactive(ctx);
    }
}
