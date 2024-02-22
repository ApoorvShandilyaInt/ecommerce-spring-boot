package UpIn.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import UpIn.entity.OrderItem;

public interface OrderItemRepository extends JpaRepository<OrderItem, Long>
{
    // Add custom query methods if needed
}
