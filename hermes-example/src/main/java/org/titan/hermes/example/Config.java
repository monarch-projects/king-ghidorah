package org.titan.hermes.example;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.titan.hermes.common.bindings.Binding;
import org.titan.hermes.common.exchange.BaseExchange;
import org.titan.hermes.common.exchange.FanoutExchange;
import org.titan.hermes.common.queue.Queue;

/**
 *
 */
@Configuration
public class Config {
	@Bean
	public Queue queue() {
		return new Queue("hermesQueue");
	}

	@Bean
	public FanoutExchange exchange() {
		return new FanoutExchange("hermesExchange");
	}

	@Bean
	public Binding binding(Queue queue, BaseExchange exchange) {
		return new Binding().bind(queue).to(exchange);
	}
}
