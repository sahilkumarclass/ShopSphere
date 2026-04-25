package com.shopsphere.order.kafka;

import com.shopsphere.order.event.OrderPlacedEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class OrderEventPublisher {

    public static final String TOPIC_ORDER_PLACED = "order.placed";

    private final KafkaTemplate<String, Object> kafkaTemplate;

    public void publishOrderPlaced(OrderPlacedEvent event) {
        kafkaTemplate.send(TOPIC_ORDER_PLACED, String.valueOf(event.getOrderId()), event);
        log.info("Published order.placed event for order {}", event.getOrderId());
    }
}
