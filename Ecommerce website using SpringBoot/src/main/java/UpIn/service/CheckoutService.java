package UpIn.service;

import UpIn.entity.Checkout;

public interface CheckoutService
{
    Checkout processCheckout(Long userId, String address);

    void deleteCheckout(Long userId);

    Checkout getCheckoutByUserId(Long userId);
}