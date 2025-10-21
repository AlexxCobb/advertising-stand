package github.com.AlexxCobb.advertising_stand.service;

import github.com.AlexxCobb.advertising_stand.controller.dto.ProductEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class RabbitReceiver {

    private final ProductService productService;
    private final SimpMessagingTemplate messagingTemplate;

    private static final String TOPIC_RELOAD = "/topic/reload";

    @RabbitListener(queues = "${queue.name}")
    public void receive(ProductEvent productEvent) {
        var eventType = productEvent.eventType();
        var productId = productEvent.publicProductId();
        switch (eventType) {
            case UPDATE -> productService.updateProduct(productEvent.product());
            case DELETE -> productService.deleteProduct(productId);
        }
        messagingTemplate.convertAndSend(TOPIC_RELOAD, "PRODUCT_UPDATED");
    }
}
