package UpIn.service.impl;


import java.util.List;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import UpIn.entity.Cart;
import UpIn.entity.Checkout;
import UpIn.entity.User;
import UpIn.repository.CheckoutRepository;

import UpIn.service.CartService;
import UpIn.service.CheckoutService;
import UpIn.service.DeliveryService;
import UpIn.service.UserService;

// @AllArgsConstructor
@Service
public class CheckoutServiceImpl implements CheckoutService {

    @Autowired
    private final UserService userService;
    private final CartService cartService;
    private final CheckoutRepository checkoutRepository;
    private DeliveryService deliveryService;

    public CheckoutServiceImpl(UserService userService, CartService cartService, CheckoutRepository checkoutRepository, DeliveryService deliveryService) {
        this.userService = userService;
        this.cartService = cartService;
        this.checkoutRepository = checkoutRepository;
        this.deliveryService = deliveryService;
    }

    @Override
    public Checkout processCheckout(Long userId, String checkoutConfirmation)
    {
        // if (!"true".equals(checkoutConfirmation)) {
        //     throw new IllegalArgumentException("Invalid checkout confirmation");
        // }

        String address = deliveryService.getAddressByUserId(userId);
        if (address == null) {
            throw new RuntimeException("Delivery address not found");
        }

        User user = userService.getUserById(userId);
        List<Cart> cartItems = cartService.getCartItems(userId);
        String billId = generateBillId();
        
        // Overwrite earlier checkout for the same user
        Checkout existingCheckout = checkoutRepository.findByUser_Id(userId);
        Checkout checkout = existingCheckout != null ? existingCheckout : new Checkout();
        checkout.setUser(user);
        checkout.setCartItems(cartItems);
        checkout.setBillId(billId);
        checkout.setAddress(address);
        checkoutRepository.save(checkout);
        return checkout;
    }

    private String generateBillId() {
        // Generate unique bill id using Random class
        Random random = new Random();
        StringBuilder billIdBuilder = new StringBuilder();
        String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        for (int i = 0; i < 5; i++) {
            int randomIndex = random.nextInt(characters.length());
            char randomChar = characters.charAt(randomIndex);
            billIdBuilder.append(randomChar);
        }
        return billIdBuilder.toString();
    }
    
    @Override
    public void deleteCheckout(Long userId) {
        checkoutRepository.deleteByUser_Id(userId);
    }

    @Override
    public Checkout getCheckoutByUserId(Long userId) {
        return checkoutRepository.findByUser_Id(userId);
    }

}