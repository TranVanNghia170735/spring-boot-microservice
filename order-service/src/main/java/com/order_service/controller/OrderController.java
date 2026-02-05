package com.order_service.controller;


import com.order_service.client.UserClient;
import com.order_service.dto.OrderResponse;
import com.order_service.dto.UserDto;
import com.order_service.model.Order;
import com.order_service.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderRepository orderRepository;
    private final UserClient userClient;

    @PostMapping
    public Order createOrder (@RequestBody Order order){
        return orderRepository.save(order);
    }

    @GetMapping
    public List<Order> getAllOrders(){
        return orderRepository.findAll();
    }

    @GetMapping("/{id}")
    public OrderResponse getOrderById(@PathVariable Long id) {
        Order order = orderRepository.findById(id).orElseThrow();

        // Gọi user-service bằng Feign
        UserDto user = userClient.getUserById(order.getUserId());

        return new OrderResponse(order.getId(), order.getProduct(), order.getPrice(), user);
    }

}
