package org.titan.hermes.client.aspect;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.titan.hermes.client.temlate.HermesTemplate;
import org.titan.hermes.common.message.Message;

/**
 * @Title: HermesListenerAspect
 * @Description:
 * @Author: daozhang
 * @date: 2019/9/1
 */
@Aspect
@Configuration
public class HermesListenerAspect {

    @Autowired
    private HermesTemplate template;

    @Around("@annotation(org.titan.hermes.common.annotations.HermesListener)")
    public void process(ProceedingJoinPoint point) throws Throwable {
        point.proceed();
        Object[] objects = point.getArgs();
        for (Object o : objects) {
            if (o instanceof Message) {
                this.template.sendAck((Message) o);
                break;
            }
        }
    }

}
