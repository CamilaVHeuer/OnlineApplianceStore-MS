package com.camicompany.shopping_cart_service.dto;

import com.camicompany.shopping_cart_service.model.CartStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CartDTO {
    @Schema(description = "Unique identifier of the cart", example = "1", accessMode = Schema.AccessMode.READ_ONLY)
    private Long id;
    @Schema(description = "Identifier of the user who owns the cart", example = "1500.00", accessMode = Schema.AccessMode.READ_ONLY)
    private Double totalPrice;
    @Schema(description = "List of items in the cart")
    private List<CartItemDTO> items;
    @Schema(description = "Status of the cart", example = "CREATED", accessMode = Schema.AccessMode.READ_ONLY)
    private CartStatus status;
}
