package UpIn.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CartDto
{
    @JsonInclude(Include.NON_NULL) private Long userId;
    @JsonInclude(Include.NON_NULL)private Long productId;
    private int quantity;
    private int productPrice;
    private int totalPrice;

    private String productName;
    private int price;

}