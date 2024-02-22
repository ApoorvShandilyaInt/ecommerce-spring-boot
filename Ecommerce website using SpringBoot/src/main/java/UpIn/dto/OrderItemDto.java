package UpIn.dto;

import UpIn.entity.Product;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class OrderItemDto
{
    private String productName;
    private int quantity;
    private int price;
    private Product product;
    private double totalPrice;
}