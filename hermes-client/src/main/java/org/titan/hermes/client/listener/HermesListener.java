package org.titan.hermes.client.listener;

import lombok.Data;
import lombok.experimental.Accessors;
import org.titan.hermes.client.meta.ListenerMetaInfo;

import java.lang.reflect.Method;

/**
 * @Title: HermesListener
 * @Description:
 * @Author: daozhang
 * @date: 2019/9/1
 */
@Data
@Accessors(chain = true)
public class HermesListener {

    private ListenerMetaInfo info;
    private Method handler;
    private Object bean;
    private Class<?> clazz;
    private String name;
    private Class<?> returnType;
    private Class<?>[] parameterTypes;
}
