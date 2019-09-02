package org.titan.hermes.common.exchange;

/**
 * @Title: Exchange
 * @Description:
 * @Author: daozhang
 * @date: 2019/9/1
 */
public interface Exchange {

    String EXCHANGE_FANOUT = "FANOUT";

    String getName();
    String getType();
    boolean autoDelete();
}
