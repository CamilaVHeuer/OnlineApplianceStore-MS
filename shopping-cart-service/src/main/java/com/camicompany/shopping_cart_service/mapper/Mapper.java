package com.camicompany.shopping_cart_service.mapper;

import com.camicompany.shopping_cart_service.dto.CartDTO;
import com.camicompany.shopping_cart_service.dto.CartItemDTO;
import com.camicompany.shopping_cart_service.model.Cart;
import com.camicompany.shopping_cart_service.model.CartItem;

import java.util.List;

public class Mapper {
    public static CartItemDTO toDTO(CartItem ci){
        if (ci==null){
            return null;
        }
        return CartItemDTO.builder().
                itemId(ci.getItemId()).
                productId(ci.getProductId())
                .quantity(ci.getQuantity())
                .build();

    }


    public static CartDTO toDTO(Cart c){
        if (c==null){
            return null;
        }
        List<CartItemDTO> itemDTO = c.getItems().stream()
                .map(Mapper::toDTO)
                .toList();
        return CartDTO.builder().
                id(c.getId()).
                totalPrice(c.getTotalPrice())
                .items(itemDTO)
                .build();

    }
}
