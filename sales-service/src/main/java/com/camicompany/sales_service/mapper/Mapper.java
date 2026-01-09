package com.camicompany.sales_service.mapper;

import com.camicompany.sales_service.dto.SaleDTO;
import com.camicompany.sales_service.model.Sale;

public class Mapper {

    public static SaleDTO toDTO(Sale sale){
        if (sale==null){
            return null;
        }
        return SaleDTO.builder().
                saleId(sale.getSaleId()).
                date(sale.getDate())
                .cartId(sale.getCartId())
                .totalAmount(sale.getTotalAmount())
                .status(sale.getStatus())
                .build();
    }
}
