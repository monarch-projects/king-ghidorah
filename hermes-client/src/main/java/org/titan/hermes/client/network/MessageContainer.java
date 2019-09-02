package org.titan.hermes.client.network;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.titan.hermes.client.listener.HermesListerContainer;
import org.titan.hermes.common.message.HermesMessage;

import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @Title: MessageContainer
 * @Description:
 * @Author: daozhang
 * @date: 2019/9/1
 */
@Configuration
public class MessageContainer {
    @Autowired
    private HermesListerContainer container;

    private static final Map<Long, ResponseFuture<?>> MAP = new ConcurrentHashMap<>(32, 4.0F);

    @SuppressWarnings("unchecked")
    public void receive(HermesMessage message) {
        Object res = this.container.getConvertor().decode(message);
        Long id = message.getId();
        ResponseFuture future = MAP.get(id);
        if (Objects.nonNull(future))
            future.receive(res);
    }

    public void add(Long id, ResponseFuture<?> future) {
        MAP.put(id, future);
    }
}
