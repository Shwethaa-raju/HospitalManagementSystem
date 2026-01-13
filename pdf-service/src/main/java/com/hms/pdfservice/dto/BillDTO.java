package com.hms.pdfservice.dto;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

public class BillDTO {

    private UUID billId;
    private UUID appointmentId;
    private BigDecimal totalAmount;
    private List<BillItemDTO> items;

    public UUID getBillId() {
        return billId;
    }

    public void setBillId(UUID billId) {
        this.billId = billId;
    }

    public UUID getAppointmentId() {
        return appointmentId;
    }

    public void setAppointmentId(UUID appointmentId) {
        this.appointmentId = appointmentId;
    }

    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
    }

    public List<BillItemDTO> getItems() {
        return items;
    }

    public void setItems(List<BillItemDTO> items) {
        this.items = items;
    }
}

