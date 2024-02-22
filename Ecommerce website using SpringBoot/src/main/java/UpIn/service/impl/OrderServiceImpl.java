package UpIn.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import UpIn.entity.Cart;
// import UpIn.entity.Delivery;
import UpIn.entity.Order;
import UpIn.entity.OrderItem;
import UpIn.entity.User;
import UpIn.repository.OrderRepository;
import UpIn.service.CartService;
import UpIn.service.CheckoutService;
// import UpIn.service.DeliveryService;
import UpIn.service.OrderService;
import UpIn.service.UserService;
import lombok.AllArgsConstructor;

@AllArgsConstructor
@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    private OrderRepository orderRepository;
    private UserService userService;
    // private DeliveryService deliveryService;
    private CartService cartService;
    private CheckoutService checkoutService;

    @Override
    @Transactional
    public Order placeOrder(Long userId, String billId, int totalAmount) {
        // Retrieve user and delivery information
        User user = userService.getUserById(userId);
        // Delivery delivery = deliveryService.getDeliveryByUserId(userId);
    
        // Create order
        Order order = new Order();
        order.setOrderNumber(generateOrderNumber());
        order.setUser(user);
        order.setTotalAmountPaid(totalAmount);
    
        // Add order items from cart items
        List<Cart> cartItems = cartService.getCartItems(userId);
        List<OrderItem> orderItems = new ArrayList<>();
        for (Cart cartItem : cartItems) {
            OrderItem orderItem = new OrderItem();
            orderItem.setProduct(cartItem.getProduct());
            orderItem.setQuantity(cartItem.getQuantity());
            orderItem.setPrice(cartItem.getProduct().getPrice());
            orderItems.add(orderItem);
        }
        order.setOrderItems(orderItems);
        order = orderRepository.save(order);
    
        // Delete cart items
        cartService.deleteCartItem(userId);
        
        // Delete checkout
        checkoutService.deleteCheckout(userId);
    
        return order;
    }

    private String generateOrderNumber() {
        // Generate unique order number using Random class
        Random random = new Random();
        StringBuilder orderNumberBuilder = new StringBuilder();
        String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        for (int i = 0; i < 10; i++) {
            int randomIndex = random.nextInt(characters.length());
            char randomChar = characters.charAt(randomIndex);
            orderNumberBuilder.append(randomChar);
        }
        return orderNumberBuilder.toString();
    }
}