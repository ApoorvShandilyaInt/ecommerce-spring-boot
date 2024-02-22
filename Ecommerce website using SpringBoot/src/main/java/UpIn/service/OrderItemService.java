package UpIn.service;

import UpIn.dto.OrderItemDto;
import UpIn.entity.Order;
import UpIn.entity.OrderItem;

public interface OrderItemService
{
    OrderItem createOrderItem(OrderItemDto orderItemDto, Order order);
}