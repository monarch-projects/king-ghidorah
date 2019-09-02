package org.titan.hermes.client.network;

/**
 * @Title: Future
 * @Description:
 * @Author: daozhang
 * @date: 2019/9/1
 */
public interface Future<T> {
    boolean ok();

    T get();

    T get(long time);
}
