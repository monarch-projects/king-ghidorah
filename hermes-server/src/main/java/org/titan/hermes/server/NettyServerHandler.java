package org.titan.hermes.server;

import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import org.titan.hermes.common.convertor.DefaultJsonConvertor;
import org.titan.hermes.common.convertor.MessageConvertor;
import org.titan.hermes.common.message.HermesMessage;
import org.titan.hermes.common.message.Message;

/**
 * Created with IntelliJ IDEA.
 * User: daozhang
 * Time: 2019/6/23
 * Description:
 */
@ChannelHandler.Sharable
public class NettyServerHandler extends SimpleChannelInboundHandler<HermesMessage> {

    @Override
    public void channelInactive(ChannelHandlerContext ctx) {
        System.out.println("链接断开");
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, HermesMessage o) throws Exception {
        System.out.println(o.toString());
        Object object = new DefaultJsonConvertor().decode(o);
        System.out.println();

    }


}
