package UpIn.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "cart")
public class Cart {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne
    private User user;
    
    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;

    @Column(name = "quantity")
    private int quantity;

    @Column(name = "total_price")
    private int totalPrice; // Total price as integer

    // @ManyToOne
    // @JoinColumn(name = "checkout_id")
    // private Checkout checkout;

    public void updateTotalPrice() {
        this.totalPrice = this.product.getPrice() * this.quantity;
    }
    
    // Constructors, Getter and Setter methods
}