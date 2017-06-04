package org.aamkonsult.rabbitmq;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.ExchangeTypes;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
@EnableScheduling
public class RabbitMessenger {

    private static final Logger log = LoggerFactory.getLogger(RabbitMessenger.class);

    private static final String EXCHANGE_NAME = "rabbitWorkshop";

    private RabbitTemplate template;

    public RabbitMessenger(RabbitTemplate template) {
        this.template = template;
    }

    @RabbitListener(
            bindings = @QueueBinding(
                    value = @Queue(),
                    exchange = @Exchange(value = EXCHANGE_NAME, durable = "false", type = ExchangeTypes.FANOUT)))
    public void onMessage(Message message) {
        log.info("Message as generic: {}", message);
    }

    @RabbitListener(
            bindings = @QueueBinding(
                    value = @Queue(),
                    exchange = @Exchange(value = EXCHANGE_NAME, durable = "false", type = ExchangeTypes.FANOUT)))
    public void onMessage(String message) {
        log.info("Message as String: {}", message);
    }


    @Scheduled(cron = "0/10 * * * * *")
    public void sendMessage() {
        template.convertAndSend(EXCHANGE_NAME, "", new Date().toString());
        log.info("Message sent: {}", new Date().toString());
    }

}
