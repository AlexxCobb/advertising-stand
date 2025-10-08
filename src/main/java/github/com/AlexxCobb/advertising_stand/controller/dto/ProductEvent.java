package github.com.AlexxCobb.advertising_stand.controller.dto;

import github.com.AlexxCobb.advertising_stand.controller.dto.enums.EventType;

public record ProductEvent(
        EventType eventType,
        String publicProductId,
        ProductForStandDto product,
        Long timestamp
) {
}
