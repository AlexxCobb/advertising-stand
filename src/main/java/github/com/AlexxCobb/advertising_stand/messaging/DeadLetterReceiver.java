package github.com.AlexxCobb.advertising_stand.messaging;

import github.com.AlexxCobb.advertising_stand.controller.dto.ProductEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class DeadLetterReceiver {

    @RabbitListener(queues = "product.dead.queue")
    public void handleDeadLetter(
            ProductEvent productEvent,
            Message message) {
        var xDeath = message.getMessageProperties().getXDeathHeader();
        if (xDeath != null && !xDeath.isEmpty()) {
            var firstDeath = xDeath.get(0);
            var reason = firstDeath.get("reason");
            var routingKeys = firstDeath.get("routingKey");

            log.error(
                    "Dead letter received: eventId={}, type={}, productId={}, reason={}, routingKeys={}",
                    productEvent.eventId(),
                    productEvent.eventType(),
                    productEvent.publicProductId(),
                    routingKeys,
                    reason
            );
        }
    }
}