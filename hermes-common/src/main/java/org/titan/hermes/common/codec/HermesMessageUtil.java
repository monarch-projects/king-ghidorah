package org.titan.hermes.common.codec;

import com.dyuproject.protostuff.LinkedBuffer;
import com.dyuproject.protostuff.ProtobufIOUtil;
import com.dyuproject.protostuff.runtime.RuntimeSchema;
import org.titan.hermes.common.message.HermesMessage;
import org.titan.hermes.common.message.Message;

/**
 * @Title: HermesMessageUtil
 * @Description:
 * @Author: daozhang
 * @date: 2019/9/1
 */
public class HermesMessageUtil {

    private static final RuntimeSchema<HermesMessage> BASE_MESSAGE_SCHEMA = RuntimeSchema.createFrom(HermesMessage.class);

    private static final RuntimeSchema<Message> MESSAGE_SCHEMA = RuntimeSchema.createFrom(Message.class);

    public static byte[] encodeBaseMessage(HermesMessage message) {
        return ProtobufIOUtil
                .toByteArray(
                        message, BASE_MESSAGE_SCHEMA, LinkedBuffer.allocate(LinkedBuffer.DEFAULT_BUFFER_SIZE)
                );
    }

    static byte[] encodeMessage(Message message) {
        return ProtobufIOUtil
                .toByteArray(
                        message, MESSAGE_SCHEMA, LinkedBuffer.allocate(LinkedBuffer.DEFAULT_BUFFER_SIZE)
                );
    }

    static HermesMessage decodeBaseMessage(byte[] bytes) {
        HermesMessage message = BASE_MESSAGE_SCHEMA.newMessage();
        ProtobufIOUtil.mergeFrom(bytes, message, BASE_MESSAGE_SCHEMA);
        return message;
    }

    static Message decodeMessage(byte[] bytes) {
        Message message = MESSAGE_SCHEMA.newMessage();
        ProtobufIOUtil.mergeFrom(bytes, message, MESSAGE_SCHEMA);
        return message;
    }


}
