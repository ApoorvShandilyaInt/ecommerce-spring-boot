package UpIn.service;

import UpIn.dto.DeliveryDto;
import UpIn.entity.Delivery;


public interface DeliveryService
{
    Delivery saveOrUpdateDelivery(DeliveryDto deliveryDto, Long userId);
    
    Delivery getDeliveryByUserId(Long userId);

    public String getAddressByUserId(Long userId);
}
