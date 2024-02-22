package UpIn.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import UpIn.entity.Product;

public interface ProductRepository extends JpaRepository<Product, Long>
{
    Product findByProductName(String productName);
}
