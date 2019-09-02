package org.titan.hermes.common.message;

import lombok.Data;
import lombok.experimental.Accessors;
import org.titan.hermes.common.util.IdUtil;

import java.util.Optional;

/**
 * @Title: HermesMessage
 * @Description:
 * @Author: daozhang
 * @date: 2019/9/1
 */
@Data
@Accessors(chain = true)
public class HermesMessage extends Message {

    public static final short INIT_CONFIG = 1;
    public static final short USER = 2;

    public static final short HEART_BEAT = 3;

    public static final short CONSUME_ACK = 4;

    public static final short SEND_MESSAGE_ACK = 5;

    public static final short ACK_CONFIRM = 6;

    public static final short SERVER_EXCHANGE_BINDINGS = 7;

    private String queueName;
    private String exchange;
    private String key;

    private short type;


    public void setMessage(Message message) {
        this.setId(Optional.ofNullable(message.getId()).orElse(IdUtil.snowFakeId()));
        if (message.getToClass() == null)
            throw new NullPointerException();
        this.setToClass(message.getToClass()).setMessage(message.getMessage());
    }
}
