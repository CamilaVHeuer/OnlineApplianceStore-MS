package com.camicompany.sales_service.mapper;

import com.camicompany.sales_service.dto.SaleResponseDTO;
import com.camicompany.sales_service.model.Sale;

public class Mapper {

    public static SaleResponseDTO toDTO(Sale sale){
        if (sale==null){
            return null;
        }
        return new SaleResponseDTO(
                sale.getSaleId(),
                sale.getDate(),
                sale.getUsername(),
                sale.getCartId(),
                sale.getTotalAmount(),
                sale.getStatus());

    }
}
