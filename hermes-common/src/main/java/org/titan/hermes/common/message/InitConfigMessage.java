package org.titan.hermes.common.message;

import lombok.Data;
import lombok.experimental.Accessors;
import org.titan.hermes.common.bindings.Binding;
import org.titan.hermes.common.exchange.BaseExchange;
import org.titan.hermes.common.queue.Queue;

import java.util.ArrayList;
import java.util.List;

/**
 * @Title: InitConfigMessage
 * @Description:
 * @Author: daozhang
 * @date: 2019/9/1
 */
@Data
@Accessors(chain = true)
public class InitConfigMessage {

    private List<Queue> queues = new ArrayList<>();

    private List<BaseExchange> exchanges = new ArrayList<>();

    private List<Binding> bindings = new ArrayList<>();

    private List<String> listenQueues = new ArrayList<>();

}
