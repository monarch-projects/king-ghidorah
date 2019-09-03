package org.titan.hermes.client;

import lombok.extern.slf4j.Slf4j;
import org.springframework.aop.support.AopUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.EnvironmentAware;;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.core.env.Environment;
import org.springframework.retry.annotation.EnableRetry;
import org.titan.hermes.client.listener.HermesListerContainer;
import org.titan.hermes.client.meta.ListenerMetaInfo;
import org.titan.hermes.common.annotations.HermesListener;
import org.titan.hermes.common.bindings.Binding;
import org.titan.hermes.common.exchange.BaseExchange;
import org.titan.hermes.common.exchange.Exchange;
import org.titan.hermes.common.message.HermesMessage;
import org.titan.hermes.common.message.InitConfigMessage;
import org.titan.hermes.common.queue.Queue;
import org.titan.hermes.common.util.JsonUtil;

import java.lang.reflect.Method;
import java.util.*;

/**
 * @Title: HermesInitlization
 * @Description:
 * @Author: daozhang
 * @date: 2019/9/1
 */
@Slf4j
@EnableRetry
@Configuration
public class HermesInitlization implements BeanPostProcessor, EnvironmentAware, ApplicationListener<ApplicationStartedEvent> {
    @Autowired
    private HermesListerContainer listerContainer;
    @Autowired
    private HermesClientBootStrap bootStrap;

    private List<org.titan.hermes.client.listener.HermesListener> list = new ArrayList<>();

    private InitConfigMessage configMessage = new InitConfigMessage();

    private Set<String> listenQueues = new HashSet<>();

	@Override
	public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
		return bean;
	}

	@Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
		Class<?> clz = AopUtils.getTargetClass(bean);
        Method[] methods = clz.getDeclaredMethods();
        for (Method method : methods) {
			if (method.isAnnotationPresent(HermesListener.class)) {
                log.info("find hermes handler,{}:{}", beanName, method.getName());
                this.processListener(method, bean);
            }
        }

        if (bean instanceof Queue) {
            log.info("find queue,{}", bean);
            this.configMessage.getQueues().add((Queue) bean);
        }

        if (bean instanceof Exchange) {
            log.info("find exchange,{}", bean);
            this.configMessage.getExchanges().add((BaseExchange) bean);
        }

        if (bean instanceof Binding) {
            log.info("find binding,{}", bean);
            this.configMessage.getBindings().add((Binding) bean);
        }

        return bean;
    }


    public void processListener(Method method, Object bean) {
        HermesListener listener = AnnotationUtils.findAnnotation(method, HermesListener.class);
        org.titan.hermes.client.listener.HermesListener hermesListener = new org.titan.hermes.client.listener.HermesListener();
        ListenerMetaInfo info = ListenerMetaInfo.of(Arrays.asList(listener.queues()), listener.key());

        hermesListener.setInfo(info).setClazz(bean.getClass()).setBean(bean).setHandler(method).setParameterTypes(method.getParameterTypes())
                .setReturnType(method.getReturnType());

        this.listenQueues.addAll(Arrays.asList(listener.queues()));
        this.list.add(hermesListener);
    }

    private Environment environment;

    @Override
    public void setEnvironment(Environment environment) {
        this.environment = environment;
    }


    @Override
    public void onApplicationEvent(ApplicationStartedEvent applicationStartedEvent) {
        list.forEach(this.listerContainer::addListener);
        String host = Optional.ofNullable(this.environment.getProperty("org.titan.hermes.server-host")).orElse("localhost");
        String port = Optional.ofNullable(this.environment.getProperty("org.titan.hermes.server-port")).orElse("12306");
        this.bootStrap.connect(host, Integer.valueOf(port));
        this.configMessage.getListenQueues().addAll(this.listenQueues);

        this.bootStrap.getChannel().writeAndFlush(new HermesMessage().setType(HermesMessage.INIT_CONFIG).setToClass(InitConfigMessage.class)
                .setMessage(JsonUtil.encode(this.configMessage).getBytes()));
    }
}
