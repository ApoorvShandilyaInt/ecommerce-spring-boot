package UpIn.service.impl;

import org.springframework.stereotype.Service;

import UpIn.dto.DeliveryDto;
import UpIn.entity.Delivery;
import UpIn.repository.DeliveryRepository;
import UpIn.service.DeliveryService;
import lombok.AllArgsConstructor;

@AllArgsConstructor
@Service
public class DeliveryServiceImpl implements DeliveryService {

    private final DeliveryRepository deliveryRepository;

    @Override
    public Delivery saveOrUpdateDelivery(DeliveryDto deliveryDto, Long userId) {
        try {
            Delivery existingDelivery = deliveryRepository.findByUserId(userId).orElse(null);

            if (existingDelivery != null) {
                // Update existing address
                existingDelivery.setAddress(deliveryDto.getAddress());
                return deliveryRepository.save(existingDelivery);
            } else {
                // Save new address
                Delivery newDelivery = new Delivery();
                newDelivery.setUserId(userId);
                newDelivery.setAddress(deliveryDto.getAddress());
                return deliveryRepository.save(newDelivery);
            }
        } catch (Exception e) {
            // Log the exception or handle it appropriately
            e.printStackTrace();
            throw new RuntimeException("Failed to save or update delivery information");
        }
    }

    @Override
    public Delivery getDeliveryByUserId(Long userId) {
        return deliveryRepository.findByUserId(userId).orElse(null);
    }

    @Override
    public String getAddressByUserId(Long userId) {
        Delivery delivery = deliveryRepository.findByUserId(userId).orElse(null);
        return delivery != null ? delivery.getAddress() : null;
    }
}