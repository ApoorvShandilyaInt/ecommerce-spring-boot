package UpIn.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import UpIn.dto.OrderItemDto;
import UpIn.entity.Order;
import UpIn.entity.OrderItem;
import UpIn.repository.OrderItemRepository;
import UpIn.service.OrderItemService;
import lombok.AllArgsConstructor;

@AllArgsConstructor
@Service
public class OrderItemServiceImpl implements OrderItemService {

    @Autowired
    private OrderItemRepository orderItemRepository;

    @Override
    @Transactional
    public OrderItem createOrderItem(OrderItemDto orderItemDto, Order order) {
        OrderItem orderItem = new OrderItem();
        orderItem.setOrder(order);
        orderItem.setProductName(orderItemDto.getProductName());
        orderItem.setQuantity(orderItemDto.getQuantity());
        orderItem.setPrice(orderItemDto.getPrice());
        return orderItemRepository.save(orderItem);
    }
}