package org.titan.hermes.common.bindings;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.assertj.core.util.Strings;
import org.titan.hermes.common.exchange.BaseExchange;
import org.titan.hermes.common.queue.Queue;

import java.util.Objects;

/**
 * @Title: Binding
 * @Description:
 * @Author: daozhang
 * @date: 2019/9/1
 */
@EqualsAndHashCode
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Binding {
    private Queue queue;
    private BaseExchange exchange;
    private String key;

    public Binding bind(Queue queue) {
        if (Objects.isNull(queue))
            throw new NullPointerException("queue can't be null !");
        this.queue = queue;
        return this;
    }

    public Binding to(BaseExchange exchange) {
        if (Objects.isNull(exchange))
            throw new NullPointerException("exchange can't be null !");
        this.exchange = exchange;
        return this;
    }

    public Binding key(String key) {
        if (Strings.isNullOrEmpty(key))
            throw new NullPointerException("exchange can't be null !");
        this.key = key;
        return this;
    }


}
