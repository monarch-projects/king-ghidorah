package org.titan.hermes.example;

import org.springframework.stereotype.Component;
import org.titan.hermes.common.annotations.HermesListener;
import org.titan.hermes.common.message.Message;

@Component
public class HermersBean {
	@HermesListener(queues = {"hermesQueue"}, key = "aaa")
	public void process(Message message) {

	}
}
