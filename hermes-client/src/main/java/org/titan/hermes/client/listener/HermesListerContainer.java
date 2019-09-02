package org.titan.hermes.client.listener;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Configuration;
import org.titan.hermes.client.meta.ListenerMetaInfo;
import org.titan.hermes.common.bindings.Bindings;
import org.titan.hermes.common.convertor.DefaultJsonConvertor;
import org.titan.hermes.common.convertor.MessageConvertor;
import org.titan.hermes.common.exception.HermesException;
import org.titan.hermes.common.exchange.Exchange;
import org.titan.hermes.common.message.HermesMessage;

import java.lang.reflect.Method;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * @Title: HermesListerContainer
 * @Description:
 * @Author: daozhang
 * @date: 2019/9/1
 */
@Slf4j
@Configuration
public class HermesListerContainer implements ApplicationContextAware {

    private Map<String, HermesListener> listeners = new ConcurrentHashMap<>();

    private Map<Class<?>, Object> beanMap = new ConcurrentHashMap<>();

    private Map<String, String> exchangeType = new HashMap<>();


    @Autowired(required = false)
    private MessageConvertor convertor;

    public void addListener(HermesListener listener) {
        log.info("add listener :{}", listener);
        ListenerMetaInfo info = listener.getInfo();
        info.getQueueNames().forEach(queue -> {
            this.listeners.put(queue + "_" + info.getKey(), listener);
        });
    }


    public void excuterMessage(HermesMessage message) throws Exception {
        if (message.getType() != HermesMessage.USER) {
            log.error("error to execute message ,because this is system message");
            return;
        }

        if (exchangeType.get(message.getExchange()).equals(Exchange.EXCHANGE_FANOUT)) {
            List<HermesListener> list = this.listeners.entrySet().stream()
                    .filter(e ->
                            e.getKey().startsWith(message.getQueueName()))
                    .map(Map.Entry::getValue)
                    .collect(Collectors.toList());

            if (Objects.isNull(message.getMessage())) {
                this.execute(list, null);
                return;
            } else {
                Object object = this.getConvertor().decode(message);
                this.execute(list, object);
                return;
            }
        }

        HermesListener listener = this.listeners.get(message.getQueueName() + "_" + message.getKey());
        List<HermesListener> listeners = Collections.singletonList(listener);
        if (Objects.isNull(message.getMessage())) {
            this.execute(listeners, null);
        } else {
            this.execute(listeners, this.getConvertor().decode(message));
        }
    }


    private void execute(List<HermesListener> list, Object param) {
        if (!list.isEmpty()) {
            HermesListener listener = list.get(0);
            Method method = listener.getHandler();
            Object bean = this.getBean(listener.getClazz());
            if (Objects.isNull(bean))
                return;
            try {
                method.invoke(bean, param);
            } catch (Exception e) {
                log.error("error to execute message handler", e);
                throw new HermesException("error to execute message handler", e);
            }
        }
    }

    public void initServerBindings(HermesMessage message) {
        Bindings bindings = (Bindings) this.getConvertor().decode(message);
        bindings.getBindings().forEach(b -> this.exchangeType.put(b.getExchange().getName(), b.getExchange().getType()));
    }

    private Object getBean(Class<?> clz) {
        Object bean = this.beanMap.get(clz);
        if (Objects.isNull(bean)) {
            synchronized (this) {
                bean = this.context.getBean(clz);
                this.beanMap.put(clz, bean);
            }
        }
        return bean;
    }


    public MessageConvertor getConvertor() {
        MessageConvertor convertor = this.convertor;
        if (Objects.isNull(convertor)) {
            this.convertor = new DefaultJsonConvertor();
            return this.convertor;
        }
        return convertor;
    }

    private ApplicationContext context;

    @Override
    public void setApplicationContext(ApplicationContext context) throws BeansException {
        this.context = context;
    }
}
