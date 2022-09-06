package com.nurtech.orderservice.service;

import com.nurtech.orderservice.dto.OrderLineItemsDto;
import com.nurtech.orderservice.dto.OrderRequest;
import com.nurtech.orderservice.model.Order;
import com.nurtech.orderservice.model.OrderLineItems;
import com.nurtech.orderservice.repository.OrderRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class OrderService {

    private final OrderRepository orderRepository;

    public OrderService(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    public void orderPlace(OrderRequest orderRequest) {
        if (orderRequest.getOrderLineItems() == null) {
            throw new NullPointerException("order line items null");
        }
        Order order = new Order();
        String orderNumber = java.util.UUID.randomUUID().toString();
        order.setOrderNumber(orderNumber);

        List<OrderLineItems> orderLineItems = orderRequest.getOrderLineItems().stream()
                .map(o -> mapToObj(o)).collect(Collectors.toList());
        order.setOrderLineItems(orderLineItems);
        orderRepository.save(order);
        log.info("order placed with number {}", orderNumber);
    }

    private OrderLineItems mapToObj(OrderLineItemsDto o) {
        OrderLineItems orderLineItems = new OrderLineItems();
        orderLineItems.setSkuCode(o.getSkuCode());
        orderLineItems.setPrice(o.getPrice());
        orderLineItems.setQuantity(o.getQuantity());
        return orderLineItems;
    }
}
