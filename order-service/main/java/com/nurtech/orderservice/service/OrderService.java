package com.nurtech.orderservice.service;

import com.nurtech.orderservice.dto.InventoryResponse;
import com.nurtech.orderservice.dto.OrderLineItemsDto;
import com.nurtech.orderservice.dto.OrderRequest;
import com.nurtech.orderservice.model.Order;
import com.nurtech.orderservice.model.OrderLineItems;
import com.nurtech.orderservice.repository.OrderRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@Slf4j
public class OrderService {

    private final OrderRepository orderRepository;
    private final WebClient webClient;

    public OrderService(OrderRepository orderRepository, WebClient webClient) {
        this.orderRepository = orderRepository;
        this.webClient = webClient;
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

        List<String> skuCodes = order.getOrderLineItems().stream().map(OrderLineItems::getSkuCode).collect(Collectors.toList());

        InventoryResponse[] inventoryResponses = webClient
                .get()
                .uri("http://localhost:8084/api/inventory", uriBuilder -> uriBuilder.queryParam("skuCode", skuCodes).build())
                .retrieve()
                .bodyToMono(InventoryResponse[].class)
                .block();

        boolean match = Stream.of(inventoryResponses).allMatch(InventoryResponse::getIsInStock);
        if (match) {
            orderRepository.save(order);
            log.info("order placed with number {}", orderNumber);
        } else {
            throw new IllegalArgumentException("product is not stock, please try again letter");
        }
    }

    private OrderLineItems mapToObj(OrderLineItemsDto o) {
        OrderLineItems orderLineItems = new OrderLineItems();
        orderLineItems.setSkuCode(o.getSkuCode());
        orderLineItems.setPrice(o.getPrice());
        orderLineItems.setQuantity(o.getQuantity());
        return orderLineItems;
    }
}
