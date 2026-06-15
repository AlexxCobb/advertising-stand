package github.com.AlexxCobb.advertising_stand.messaging;

import github.com.AlexxCobb.advertising_stand.controller.dto.ProductEvent;
import github.com.AlexxCobb.advertising_stand.dao.ProcessedEvent;
import github.com.AlexxCobb.advertising_stand.dao.ProcessedEventRepository;
import github.com.AlexxCobb.advertising_stand.service.ProductService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

import java.time.Instant;

@Slf4j
@Component
@RequiredArgsConstructor
public class RabbitReceiver {

    private final ProductService productService;
    private final SimpMessagingTemplate messagingTemplate;
    private final ProcessedEventRepository processedEventRepository;

    private static final String TOPIC_RELOAD = "/topic/reload";

    @RabbitListener(queues = "${queue.name}")
    public void receive(ProductEvent productEvent) {
        if (processedEventRepository.existsByEventId(productEvent.eventId())) {
            return;
        }
        var eventType = productEvent.eventType();
        var productId = productEvent.publicProductId();
        try {
            processedEventRepository.saveAndFlush(
                    new ProcessedEvent(productEvent.eventId(), Instant.now(), eventType.name())
            );
        } catch (DataIntegrityViolationException e) {
            log.info("Duplicate event {}, skipping", productEvent.eventId());
            return;
        }

        switch (eventType) {
            case UPDATE -> productService.updateProduct(productEvent.product());
            case DELETE -> productService.deleteProduct(productId);
        }
        messagingTemplate.convertAndSend(TOPIC_RELOAD, "PRODUCT_UPDATED");
    }
}
