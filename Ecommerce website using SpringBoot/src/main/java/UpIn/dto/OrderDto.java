package UpIn.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class OrderDto {

    private String message;
    private String orderNumber;
    private String name;
    private Long mobileNumber;
    private String deliveryAddress;
    private List<OrderItemDto> orderItems;
    private int totalAmountPaid;
}
