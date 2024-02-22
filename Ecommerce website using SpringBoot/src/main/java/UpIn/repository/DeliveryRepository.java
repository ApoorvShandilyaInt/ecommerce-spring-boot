package UpIn.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import UpIn.entity.Delivery;

public interface DeliveryRepository extends JpaRepository<Delivery, Long>
{
    Optional<Delivery> findByUserId(Long userId);
}