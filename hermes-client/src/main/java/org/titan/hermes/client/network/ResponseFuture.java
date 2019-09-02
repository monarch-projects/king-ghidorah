package org.titan.hermes.client.network;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

/**
 * @Title: ResponseFuture
 * @Description:
 * @Author: daozhang
 * @date: 2019/9/1
 */
@Slf4j
public class ResponseFuture<T> implements Future<T> {

    private T result = null;

    private long id;

    private volatile boolean ok = false;

    private CountDownLatch latch = new CountDownLatch(1);

    public ResponseFuture(long id) {
        this.id = id;
    }

    public void receive(T result) {
        this.result = result;
        this.ok = true;
        this.latch.countDown();
    }

    public T get() {


        return this.get(10000L);
    }

    public T get(long time) {
        try {
            this.latch.await(time, TimeUnit.MILLISECONDS);
        } catch (InterruptedException e) {
            log.error("error to get response", e);
            throw new RuntimeException(e);
        }

        return this.result;
    }

    public boolean ok() {
        return this.ok;
    }
}
