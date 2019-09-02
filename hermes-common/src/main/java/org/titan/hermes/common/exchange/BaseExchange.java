package org.titan.hermes.common.exchange;

import lombok.Data;

/**
 * @Title: BaseExchange
 * @Description:
 * @Author: daozhang
 * @date: 2019/9/1
 */
@Data
public class BaseExchange implements Exchange {

    private String name;
    private String type;
    private boolean autoDelete;

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public String getType() {
        return this.type;
    }

    @Override
    public boolean autoDelete() {
        return this.autoDelete;
    }
}
