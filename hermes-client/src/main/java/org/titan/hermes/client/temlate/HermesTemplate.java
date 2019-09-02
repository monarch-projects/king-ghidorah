package org.titan.hermes.client.temlate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.titan.hermes.client.HermesClientBootStrap;
import org.titan.hermes.client.network.Future;
import org.titan.hermes.client.network.MessageContainer;
import org.titan.hermes.client.network.ResponseFuture;
import org.titan.hermes.common.message.HermesMessage;
import org.titan.hermes.common.message.Message;

/**
 * @Title: HermesTemplate
 * @Description:
 * @Author: daozhang
 * @date: 2019/9/1
 */
@Configuration
public class HermesTemplate {
    @Autowired
    private HermesClientBootStrap strap;
    @Autowired
    private MessageContainer container;

    public boolean sendMessageSync(String exchange, String key, Message message) {
        HermesMessage hermesMessage = new HermesMessage();
        hermesMessage.setType(HermesMessage.USER).setExchange(exchange).setKey(key).setMessage(message);
        Future<Boolean> future = new ResponseFuture<>(hermesMessage.getId());
        this.container.add(hermesMessage.getId(), (ResponseFuture<?>) future);
        this.strap.getChannel().writeAndFlush(hermesMessage);
        return future.get();
    }

    public boolean sendMessageSync(Message message) {

        return this.sendMessageSync("hermes", "hermes", message);
    }

    public boolean sendMessageSync(String exchange, Message message) {
        return this.sendMessageSync(exchange, "hermes", message);
    }

    public boolean sendAckSync(Message message) {
        return true;
    }

    public void sendAck(Message message) {

    }


    public boolean convertAndSendSync(String exchange, String key, Object message) {
        return true;
    }

    public boolean convertAndSendSync(Object message) {
        return true;
    }

    public boolean convertAndSendSync(String exchange, Object message) {
        return true;
    }

}
