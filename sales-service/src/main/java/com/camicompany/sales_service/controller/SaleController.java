package com.camicompany.sales_service.controller;

import com.camicompany.sales_service.dto.CreateSaleDTO;
import com.camicompany.sales_service.dto.SaleResponseDTO;
import com.camicompany.sales_service.dto.SaleDateDTO;
import com.camicompany.sales_service.service.ISaleService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.time.LocalDate;
import java.util.List;
@Tag(
        name = "Sales",
        description = "Public operations for sales management"
)
@RestController
@RequestMapping("/api/sales")
public class SaleController {

    @Autowired
    private ISaleService saleServ;

    @Operation(summary = "Get all sales")
    @ApiResponse(responseCode = "200", description = "List of sales returned")
    @GetMapping
    public ResponseEntity<List<SaleResponseDTO>> getAllSales(){
        return ResponseEntity.ok(saleServ.getAllSales());
    }

    @Operation(summary = "Get sales by date")
    @ApiResponse(responseCode = "200", description = "List of sales for the specified date returned")
    @GetMapping("/date/{date}")
    public ResponseEntity<List<SaleResponseDTO>> getSalesByDate(@PathVariable LocalDate date){
        return ResponseEntity.ok(saleServ.getSalesByDate(date));
    }

    @Operation(summary = "Get sale by ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Sale found and returned"),
            @ApiResponse(responseCode = "404", description = "Sale not found")
    })
    @GetMapping("/{id}")
    public ResponseEntity<SaleResponseDTO> getSaleById(@PathVariable("id") Long saleId){
        return ResponseEntity.ok(saleServ.getSaleById(saleId));
    }

    @Operation(summary = "Create a new sale")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Sale created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid sale data provided"),
            @ApiResponse(responseCode = "503", description = "Dependent service unavailable (cart or product service)"
            )
    })
    @PostMapping
    public ResponseEntity<SaleResponseDTO> createSale(@RequestBody @Valid CreateSaleDTO createSaleDTO){
        SaleResponseDTO createdSale = saleServ.createSale(createSaleDTO);
        return ResponseEntity.created(URI.create("/api/sales/" + createdSale.saleId())).body(createdSale);
    }

    @Operation(summary = "Update an existing sale")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Sale updated successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid sale data provided"),
            @ApiResponse(responseCode = "404", description = "Sale not found")
    })
    @PutMapping("/{id}")
    public ResponseEntity<SaleResponseDTO> updateSale(@PathVariable("id") Long saleId, @RequestBody @Valid SaleDateDTO saleDateDTO){
        return ResponseEntity.ok(saleServ.updateSale(saleId, saleDateDTO));
    }

    @Operation(summary = "Cancel a sale")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Sale canceled successfully"),
            @ApiResponse(responseCode = "404", description = "Sale not found"),
            @ApiResponse(responseCode = "409", description = "Sale is already cancelled"),
            @ApiResponse(responseCode = "503", description = "Dependent service unavailable (cart or product service)")
    })
    @PutMapping("/cancel/{id}")
    public ResponseEntity<SaleResponseDTO> cancelSale(@PathVariable("id") Long saleId) {
        return ResponseEntity.ok(saleServ.cancelSale(saleId));
    }
}
