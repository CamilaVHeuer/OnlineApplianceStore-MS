package com.camicompany.sales_service.dto;



import java.util.List;

public record CartDTO (
    Long id,
    Double totalPrice,
    List<CartItemDTO> items,
    String status)
{}
