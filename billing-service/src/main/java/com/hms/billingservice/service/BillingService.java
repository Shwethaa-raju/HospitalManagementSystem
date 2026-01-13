package com.hms.billingservice.service;

import com.hms.billingservice.model.Bill;
import com.hms.billingservice.model.BillItem;
import com.hms.billingservice.repository.BillItemRepository;
import com.hms.billingservice.repository.BillRepository;
import com.hms.prescriptionservice.model.Prescription;
import com.hms.prescriptionservice.repository.PrescriptionRepository;
import jakarta.transaction.Transactional;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
public class BillingService {

    private final PrescriptionRepository prescriptionRepo; // Mongo
    private final JdbcTemplate jdbcTemplate;                 // Postgres
    private final BillRepository billRepo;
    private final BillItemRepository billItemRepo;

    public BillingService(
            PrescriptionRepository prescriptionRepo,
            JdbcTemplate jdbcTemplate,
            BillRepository billRepo,
            BillItemRepository billItemRepo) {
        this.prescriptionRepo = prescriptionRepo;
        this.jdbcTemplate = jdbcTemplate;
        this.billRepo = billRepo;
        this.billItemRepo = billItemRepo;
    }

    @Transactional
    public void createBill(UUID prescriptionId) {

        Prescription prescription = prescriptionRepo.findById(prescriptionId)
                .orElseThrow(() -> new RuntimeException("Prescription not found"));

        Bill bill = new Bill();
        bill.setId(UUID.randomUUID());
        bill.setPrescriptionId(prescriptionId);
        bill.setAppointmentId(prescription.getAppointmentId());
        bill.setStatus("CREATED");

        List<BillItem> items = new ArrayList<>();
        BigDecimal total = BigDecimal.ZERO;

        // ---- MEDICINES ----
        for (Map<String, Object> med : prescription.getMedicines()) {

            UUID medicineId = (UUID) med.get("medicineId");
            int days = (int) med.get("days");

            Map<String, Object> dbRow = jdbcTemplate.queryForMap(
                    "SELECT name, price FROM medicine WHERE id = ?",
                    medicineId
            );

            BigDecimal price = (BigDecimal) dbRow.get("price");

            BillItem item = new BillItem();
            item.setId(UUID.randomUUID());
            item.setBillId(bill.getId());
            item.setItemType("MEDICINE");
            item.setReferenceId(medicineId);
            item.setName((String) dbRow.get("name"));
            item.setUnitPrice(price);
            item.setQuantity(days);
            item.setTotalPrice(price.multiply(BigDecimal.valueOf(days)));

            items.add(item);
            total = total.add(item.getTotalPrice());
        }

        // ---- LAB TESTS ----
        for (Map<String, Object> lab : prescription.getLabTests()) {

            UUID labTestId = (UUID) lab.get("labTestId");

            Map<String, Object> dbRow = jdbcTemplate.queryForMap(
                    "SELECT name, price FROM lab_test WHERE id = ?",
                    labTestId
            );

            BigDecimal price = (BigDecimal) dbRow.get("price");

            BillItem item = new BillItem();
            item.setId(UUID.randomUUID());
            item.setBillId(bill.getId());
            item.setItemType("LAB_TEST");
            item.setReferenceId(labTestId);
            item.setName((String) dbRow.get("name"));
            item.setUnitPrice(price);
            item.setQuantity(1);
            item.setTotalPrice(price);

            items.add(item);
            total = total.add(price);
        }

        bill.setTotalAmount(total);

        billRepo.save(bill);
        billItemRepo.saveAll(items);

//        return bill;
    }
}
