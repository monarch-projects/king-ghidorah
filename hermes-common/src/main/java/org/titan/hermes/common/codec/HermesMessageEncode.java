package org.titan.hermes.common.codec;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageEncoder;
import org.titan.hermes.common.consts.HermesConst;
import org.titan.hermes.common.message.HermesMessage;

import java.util.List;
import java.util.Objects;

/**
 * @Title: HermesMessageEncode
 * @Description:
 * @Author: daozhang
 * @date: 2019/9/1
 */
public class HermesMessageEncode extends MessageToMessageEncoder<HermesMessage> {

    @Override
    protected void encode(ChannelHandlerContext ctx, HermesMessage msg, List<Object> out) throws Exception {
        if (Objects.isNull(msg))
            return;

        ByteBuf byteBuf = ctx.alloc().buffer().writeBytes(HermesMessageUtil.encodeBaseMessage(msg)).writeBytes(HermesConst.DELIMITER);
        out.add(byteBuf);
    }
}
