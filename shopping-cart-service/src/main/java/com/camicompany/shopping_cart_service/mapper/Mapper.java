package com.camicompany.shopping_cart_service.mapper;

import com.camicompany.shopping_cart_service.dto.CartItemResponseDTO;
import com.camicompany.shopping_cart_service.dto.CartResponseDTO;

import com.camicompany.shopping_cart_service.model.Cart;
import com.camicompany.shopping_cart_service.model.CartItem;

import java.util.List;

public class Mapper {
    public static CartItemResponseDTO toDTO(CartItem ci){
        if (ci==null){
            return null;
        }
        return new CartItemResponseDTO(
                ci.getItemId(),
                ci.getProductId(),
                ci.getQuantity());
    }


    public static CartResponseDTO toDTO(Cart c){
        if (c==null){
            return null;
        }
        List<CartItemResponseDTO> itemDTO = c.getItems().stream()
                .map(Mapper::toDTO)
                .toList();

        return new CartResponseDTO(
                c.getId(),
                c.getUsername(),
                c.getTotalPrice(),
                itemDTO,
                c.getStatus());


    }
}
