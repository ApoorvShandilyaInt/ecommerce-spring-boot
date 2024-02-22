package UpIn.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import UpIn.entity.Order;

public interface OrderRepository extends JpaRepository<Order, Long> {
    // Add custom query methods if needed
}