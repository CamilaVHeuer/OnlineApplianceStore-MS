package com.camicompany.sales_service.repository;

import com.camicompany.sales_service.model.Sale;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface ISaleRepository extends JpaRepository<Sale, Long> {
    public List<Sale> findByDate(LocalDate date);
}
