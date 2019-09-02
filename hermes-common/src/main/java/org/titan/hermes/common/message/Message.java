package org.titan.hermes.common.message;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @Title: Message
 * @Description:
 * @Author: daozhang
 * @date: 2019/9/1
 */
@Data
@Accessors(chain = true)
public class Message {
    private byte[] message;

    private Class<?> toClass;

    private Long id;
}
