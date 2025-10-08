package github.com.AlexxCobb.advertising_stand.service;

import github.com.AlexxCobb.advertising_stand.controller.dto.ProductEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class RabbitReceiver {

    private final ProductService productService;

    @RabbitListener(queues = "${queue.name}")
    public void receive(ProductEvent productEvent) {
        var eventType = productEvent.eventType();
        var productId = productEvent.product().publicProductId();
        switch (eventType) {
            case UPDATE -> productService.updateProduct(productEvent.product());
            case DELETE -> productService.deleteProduct(productId);
        }
    }
}
