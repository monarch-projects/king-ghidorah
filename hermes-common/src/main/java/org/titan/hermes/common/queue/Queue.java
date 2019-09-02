package org.titan.hermes.common.queue;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.assertj.core.util.Strings;
import org.titan.hermes.common.exception.HermesException;

/**
 * @Title: Queue
 * @Description:
 * @Author: daozhang
 * @date: 2019/9/1
 */
@Getter
@NoArgsConstructor
@EqualsAndHashCode
public class Queue {

    private String name;

    private boolean autoDelete;

    public Queue(String name) {
        if (Strings.isNullOrEmpty(name))
            throw new HermesException("queue name can't be null or empty!");
        this.autoDelete = false;
        this.name = name;
    }

    public Queue(String name, boolean autoDelete) {
        if (Strings.isNullOrEmpty(name))
            throw new HermesException("queue name can't be null or empty!");
        this.autoDelete = autoDelete;
        this.name = name;
    }
}
