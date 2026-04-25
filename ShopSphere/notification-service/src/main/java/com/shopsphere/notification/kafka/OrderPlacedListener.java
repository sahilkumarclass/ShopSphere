package com.shopsphere.notification.kafka;

import com.shopsphere.notification.event.OrderPlacedEvent;
import com.shopsphere.notification.service.EmailService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class OrderPlacedListener {

    private final EmailService emailService;

    @KafkaListener(topics = "order.placed", groupId = "notification-service")
    public void onOrderPlaced(OrderPlacedEvent event) {
        log.info("Received order.placed for orderId={} userEmail={}", event.getOrderId(), event.getUserEmail());

        String subject = "ShopSphere — Order #" + event.getOrderId() + " confirmed";
        StringBuilder body = new StringBuilder();
        body.append("Hi,\n\nThanks for your order!\n\n");
        body.append("Order ID: ").append(event.getOrderId()).append('\n');
        body.append("Total: ₹").append(event.getTotalAmount()).append("\n\nItems:\n");
        if (event.getItems() != null) {
            event.getItems().forEach(i ->
                    body.append(" • ").append(i.getProductName())
                            .append(" x").append(i.getQuantity())
                            .append(" @ ₹").append(i.getUnitPrice()).append('\n'));
        }
        body.append("\nWe'll let you know when it ships.\n\n— The ShopSphere team");

        emailService.sendPlainEmail(event.getUserEmail(), subject, body.toString());
    }
}
