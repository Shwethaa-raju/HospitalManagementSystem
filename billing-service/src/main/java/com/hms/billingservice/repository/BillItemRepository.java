package com.hms.billingservice.repository;

import com.hms.billingservice.model.BillItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface BillItemRepository extends JpaRepository<BillItem, UUID> {
}
