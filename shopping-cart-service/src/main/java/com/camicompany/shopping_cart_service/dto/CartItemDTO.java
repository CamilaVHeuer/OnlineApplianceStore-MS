package com.camicompany.shopping_cart_service.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CartItemDTO {
    private Long itemId;
    private Long productId;
    private Integer quantity;

}
