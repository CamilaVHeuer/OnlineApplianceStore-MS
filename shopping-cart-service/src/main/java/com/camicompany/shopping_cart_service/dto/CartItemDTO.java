package com.camicompany.shopping_cart_service.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CartItemDTO {
    @Schema(description = "Unique identifier of the cart item", example = "1", accessMode = Schema.AccessMode.READ_ONLY)
    private Long itemId;
    private Long productId;
    private Integer quantity;

}
