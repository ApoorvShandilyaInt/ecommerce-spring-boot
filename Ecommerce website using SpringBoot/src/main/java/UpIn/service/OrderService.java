package UpIn.service;

import UpIn.entity.Order;

public interface OrderService
{
    Order placeOrder(Long userId, String billId, int totalAmount);
}
