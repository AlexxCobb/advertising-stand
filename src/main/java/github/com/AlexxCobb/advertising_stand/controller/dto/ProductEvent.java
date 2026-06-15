package github.com.AlexxCobb.advertising_stand.controller.dto;

import github.com.AlexxCobb.advertising_stand.controller.dto.enums.EventType;

import java.time.Instant;

public record ProductEvent(
        String eventId,
        EventType eventType,
        String publicProductId,
        ProductForStandDto product,
        Instant createdAt
) {
}
