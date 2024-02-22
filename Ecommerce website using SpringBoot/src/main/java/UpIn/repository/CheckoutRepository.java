package UpIn.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import UpIn.entity.Checkout;

public interface CheckoutRepository extends JpaRepository<Checkout, Long>
{
    Checkout findByUserId(Long userId);
    Checkout findByUser_Id(Long userId);

    void deleteByUser_Id(Long userId);
}