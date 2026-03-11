package com.hms.billingservice.service;

import com.hms.billingservice.grpc.PdfServiceGrpcClient;
import com.hms.billingservice.model.Bill;
import com.hms.billingservice.model.BillItem;
import com.hms.billingservice.repository.BillItemRepository;
import com.hms.billingservice.repository.BillRepository;
import jakarta.transaction.Transactional;
import org.bson.types.ObjectId;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;

@Service
public class BillingService {

    private final JdbcTemplate jdbcTemplate;
    private final BillRepository billRepo;
    private final BillItemRepository billItemRepo;
    private final PdfServiceGrpcClient pdfServiceGrpcClient;
    private final BillHtmlBuilder billHtmlBuilder;

    public BillingService(
            JdbcTemplate jdbcTemplate,
            BillRepository billRepo,
            BillItemRepository billItemRepo,
            PdfServiceGrpcClient pdfServiceGrpcClient,
            BillHtmlBuilder billHtmlBuilder) {

        this.jdbcTemplate = jdbcTemplate;
        this.billRepo = billRepo;
        this.billItemRepo = billItemRepo;
        this.pdfServiceGrpcClient = pdfServiceGrpcClient;
        this.billHtmlBuilder = billHtmlBuilder;
    }

    public void createBill(
            ObjectId prescriptionId,
            UUID appointmentId,
            List<Map<String, Object>> medicines,
            List<Map<String, Object>> labtests) {

        Bill bill = createBillInDb(prescriptionId, appointmentId, medicines, labtests);

        // Build HTML
        List<BillItem> items = billItemRepo.findByBillId(bill.getId());
        String html = billHtmlBuilder.buildBillHtml(bill, items);

        // Call PDF service
        String pdfUrl = pdfServiceGrpcClient.generatePdf(
                bill.getId().toString(),
                "BILL",
                html,
                "bill_" + bill.getId() + ".pdf"
        );

        // Transaction → update bill with PDF URL
        updateBillPdfUrl(bill.getId(), pdfUrl);
    }

    @Transactional
    public Bill createBillInDb(
            ObjectId prescriptionId,
            UUID appointmentId,
            List<Map<String, Object>> medicines,
            List<Map<String, Object>> labtests) {

        Bill bill = new Bill();
        bill.setId(UUID.randomUUID());
        bill.setPrescriptionId(prescriptionId);
        bill.setAppointmentId(appointmentId);
        bill.setStatus("CREATED");

        List<BillItem> items = new ArrayList<>();
        BigDecimal total = BigDecimal.ZERO;

        // ---- MEDICINES ----
        for (Map<String, Object> med : medicines) {

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
        for (Map<String, Object> lab : labtests) {

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

        return bill;
    }

    @Transactional
    public void updateBillPdfUrl(UUID billId, String pdfUrl) {

        Bill bill = billRepo.findById(billId).orElseThrow();
        bill.setPdfUrl(pdfUrl); // JPA dirty checking will update automatically
    }
}