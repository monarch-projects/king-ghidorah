package org.titan.hermes.common.exchange;

import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.util.Strings;
import org.titan.hermes.common.exception.HermesException;

/**
 * @Title: FanoutExchange
 * @Description:
 * @Author: daozhang
 * @date: 2019/9/1
 */
@Slf4j
@NoArgsConstructor
public class FanoutExchange extends BaseExchange {

    private String name;

    private String type = Exchange.EXCHANGE_FANOUT;

    private boolean autoDelete;

    public FanoutExchange(String name) {
        if (Strings.isNullOrEmpty(name))
            throw new HermesException("exchange name can't be null or empty!");
        this.autoDelete = false;
        this.name = name;
    }


    public FanoutExchange(String name, boolean autoDelete) {
        if (Strings.isNullOrEmpty(name))
            throw new HermesException("exchange name can't be null or empty!");
        this.autoDelete = autoDelete;
        this.name = name;
    }


    public String getName() {
        return this.name;
    }

    public String getType() {
        return this.type;
    }

    public boolean autoDelete() {
        return this.autoDelete;
    }
}
