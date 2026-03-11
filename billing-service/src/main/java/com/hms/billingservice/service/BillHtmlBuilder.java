package com.hms.billingservice.service;

import com.hms.billingservice.model.Bill;
import com.hms.billingservice.model.BillItem;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BillHtmlBuilder {

    public String buildBillHtml(Bill bill, List<BillItem> items) {

        StringBuilder html = new StringBuilder();

        html.append("<html>");
        html.append("<head>");
        html.append("<style>");
        html.append("body { font-family: Arial; }");
        html.append("table { border-collapse: collapse; width: 100%; margin-top:20px; }");
        html.append("th, td { border: 1px solid #ddd; padding: 8px; text-align:left; }");
        html.append("th { background-color: #f2f2f2; }");
        html.append("</style>");
        html.append("</head>");

        html.append("<body>");

        html.append("<h1>Hospital Bill</h1>");

        html.append("<p><b>Bill ID:</b> ").append(bill.getId()).append("</p>");
        html.append("<p><b>Appointment ID:</b> ").append(bill.getAppointmentId()).append("</p>");
        html.append("<p><b>Prescription ID:</b> ").append(bill.getPrescriptionId()).append("</p>");
        html.append("<p><b>Date:</b> ").append(bill.getCreatedAt()).append("</p>");

        html.append("<h2>Bill Items</h2>");

        html.append("<table>");
        html.append("<tr>");
        html.append("<th>Item Type</th>");
        html.append("<th>Name</th>");
        html.append("<th>Unit Price</th>");
        html.append("<th>Quantity</th>");
        html.append("<th>Total</th>");
        html.append("</tr>");

        for (BillItem item : items) {

            html.append("<tr>");

            html.append("<td>").append(item.getItemType()).append("</td>");
            html.append("<td>").append(item.getName()).append("</td>");
            html.append("<td>").append(item.getUnitPrice()).append("</td>");
            html.append("<td>").append(item.getQuantity()).append("</td>");
            html.append("<td>").append(item.getTotalPrice()).append("</td>");

            html.append("</tr>");
        }

        html.append("</table>");

        html.append("<h3>Total Amount: ").append(bill.getTotalAmount()).append("</h3>");

        html.append("</body>");
        html.append("</html>");

        return html.toString();
    }
}