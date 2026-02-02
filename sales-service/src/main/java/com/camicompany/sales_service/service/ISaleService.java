package com.camicompany.sales_service.service;

import com.camicompany.sales_service.dto.CreateSaleDTO;
import com.camicompany.sales_service.dto.SaleResponseDTO;
import com.camicompany.sales_service.dto.SaleDateDTO;

import java.time.LocalDate;
import java.util.List;

public interface ISaleService {

    public List<SaleResponseDTO> getAllSales();

    public SaleResponseDTO getSaleById(Long saleId);

    public List<SaleResponseDTO> getSalesByDate(LocalDate date);

    public SaleResponseDTO createSale(CreateSaleDTO saleResponseDTO);

    public SaleResponseDTO updateSale(Long saleId, SaleDateDTO saleDateDTO);

    public SaleResponseDTO cancelSale(Long saleId);


}
