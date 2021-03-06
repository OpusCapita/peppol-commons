package com.opuscapita.peppol.commons.queue;

import com.opuscapita.peppol.commons.container.ContainerMessage;
import com.opuscapita.peppol.commons.container.ContainerMessageSerializer;
import org.jetbrains.annotations.NotNull;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

@Component
@Lazy
public class RabbitMessageQueue implements MessageQueue {

    private final RabbitTemplate rabbitTemplate;
    private final ContainerMessageSerializer serializer;

    @Autowired
    public RabbitMessageQueue(@NotNull RabbitTemplate rabbitTemplate, @NotNull ContainerMessageSerializer serializer) {
        this.rabbitTemplate = rabbitTemplate;
        this.serializer = serializer;
    }

    @Override
    public void convertAndSend(@NotNull String connectionString, @NotNull ContainerMessage message) {
        ConnectionString cs = new ConnectionString(connectionString);

        String exchange = cs.getExchange();
        int delay = cs.getXDelay();

        if (exchange != null) {
            if (delay == 0) {
                // exchange defined, no delay
                rabbitTemplate.convertAndSend(exchange, cs.getQueue(), serializer.toJson(message));
            } else {
                // both exchange and delay are defined
                rabbitTemplate.convertAndSend(exchange, cs.getQueue(), serializer.toJson(message), m -> {
                    m.getMessageProperties().setDelay(delay);
                    return m;
                });
            }
        } else {
            if (delay == 0) {
                // no exchange, no delay
                rabbitTemplate.convertAndSend(cs.getQueue(), serializer.toJson(message));
            } else {
                // no exchange, delay defined
                rabbitTemplate.convertAndSend(cs.getQueue(), serializer.toJson(message), m -> {
                    m.getMessageProperties().setDelay(delay);
                    return m;
                });
            }
        }
    }
}
