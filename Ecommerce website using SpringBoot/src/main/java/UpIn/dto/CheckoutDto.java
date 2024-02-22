package UpIn.dto;

import java.time.LocalDateTime;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CheckoutDto
{
    private String name;
    private Long mobileNumber;
    private LocalDateTime dateTime;
    private List<CartDto> cartItems;
    private int totalPrice;
    private String billId;
    private String address;    
}