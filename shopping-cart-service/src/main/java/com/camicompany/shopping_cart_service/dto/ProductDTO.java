package com.camicompany.shopping_cart_service.dto;



public record ProductDTO (
    Long productId,
    Double unitPrice,
    Integer stock,
    String status){
}
