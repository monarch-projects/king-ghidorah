package org.titan.hermes.common.codec;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageDecoder;

import java.util.List;

/**
 * @Title: HermesMessageDecoder
 * @Description:
 * @Author: daozhang
 * @date: 2019/9/1
 */
public class HermesMessageDecoder extends MessageToMessageDecoder<ByteBuf> {
    @Override
    protected void decode(ChannelHandlerContext channelHandlerContext, ByteBuf byteBuf, List<Object> list) throws Exception {
        byte[] bytes = new byte[byteBuf.readableBytes()];
        byteBuf.readBytes(bytes);
        list.add(HermesMessageUtil.decodeBaseMessage(bytes));
    }
}
