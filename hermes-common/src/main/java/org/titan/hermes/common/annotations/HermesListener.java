package org.titan.hermes.common.annotations;

import java.lang.annotation.*;

/**
 * @Title: HermesListener
 * @Description:
 * @Author: daozhang
 * @date: 2019/9/1
 */
@Inherited
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface HermesListener {

    String[] queues() default {};


    String key() default "";
}
