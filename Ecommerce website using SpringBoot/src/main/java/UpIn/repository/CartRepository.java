package UpIn.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import UpIn.entity.Cart;
import UpIn.entity.Product;
import UpIn.entity.User;

public interface CartRepository extends JpaRepository<Cart, Long>
{
    Cart findByUserAndProduct(User user, Product product);
    List<Cart> findAllByUser(User user);
}