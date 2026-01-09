package com.camicompany.shopping_cart_service.dto;


import lombok.*;

import java.util.List;

@Getter @Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CartDTO {
    private Long id;
    private Double totalPrice;
    private List<CartItemDTO> items;
}
