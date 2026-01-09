package com.camicompany.sales_service.controller;

import com.camicompany.sales_service.dto.SaleDTO;
import com.camicompany.sales_service.service.ISaleService;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/sales")
public class SaleController {

    @Autowired
    private ISaleService saleServ;

    @GetMapping
    public ResponseEntity<List<SaleDTO>> getAllSales(){
        return ResponseEntity.ok(saleServ.getAllSales());
    }

    @GetMapping("/date/{date}")
    public ResponseEntity<List<SaleDTO>> getSalesByDate(@PathVariable LocalDate date){
        return ResponseEntity.ok(saleServ.getSalesByDate(date));
    }

    @GetMapping("/{id}")
    public ResponseEntity<SaleDTO> getSaleById(@PathVariable("id") Long saleId){
        return ResponseEntity.ok(saleServ.getSaleById(saleId));
    }


    @PostMapping
    public ResponseEntity<SaleDTO> createSale(@RequestBody SaleDTO saleDTO){
        SaleDTO createdSale = saleServ.createSale(saleDTO);
        return ResponseEntity.created(URI.create("/api/sales/" + createdSale.getSaleId())).body(createdSale);
    }

    @PutMapping("/{id}")
    public ResponseEntity<SaleDTO> updateSale(@PathVariable("id") Long saleId, @RequestBody SaleDTO saleDTO){
        return ResponseEntity.ok(saleServ.updateSale(saleId, saleDTO));
    }

    @PutMapping("/cancel/{id}")
    public ResponseEntity<SaleDTO> cancelSale(@PathVariable("id") Long saleId) {
        return ResponseEntity.ok(saleServ.cancelSale(saleId));
    }
}
