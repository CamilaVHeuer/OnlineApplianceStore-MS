package com.camicompany.sales_service.service;

import com.camicompany.sales_service.dto.SaleDTO;

import java.time.LocalDate;
import java.util.List;

public interface ISaleService {

    public List<SaleDTO> getAllSales();

    public SaleDTO getSaleById(Long saleId);

    public List<SaleDTO> getSalesByDate(LocalDate date);

    public SaleDTO createSale(SaleDTO saleDTO);

    public SaleDTO updateSale(Long saleId, SaleDTO saleDTO);

    public SaleDTO cancelSale(Long saleId);


}
