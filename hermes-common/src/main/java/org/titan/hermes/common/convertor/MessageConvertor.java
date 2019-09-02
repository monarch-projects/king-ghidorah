package org.titan.hermes.common.convertor;

import org.titan.hermes.common.message.Message;

/**
 * @Title: MessageConvertor
 * @Description:
 * @Author: daozhang
 * @date: 2019/9/1
 */
public interface MessageConvertor {
    Object decode(Message message);

    byte[] encode(Object message);
}
