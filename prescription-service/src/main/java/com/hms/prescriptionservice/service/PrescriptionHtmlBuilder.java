package com.hms.prescriptionservice.service;

import com.hms.prescriptionservice.model.Prescription;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class PrescriptionHtmlBuilder {

    public String buildHtml(Prescription prescription) {

        StringBuilder html = new StringBuilder();

        html.append("<html><body>");
        html.append("<h1>Hospital Prescription</h1>");

        html.append("<p><b>Appointment ID:</b> ")
                .append(prescription.getAppointmentId())
                .append("</p>");

        html.append("<p><b>Date:</b> ")
                .append(prescription.getCreatedAt())
                .append("</p>");

        html.append("<h2>Medicines</h2>");
        html.append("<table border='1'>");
        html.append("<tr><th>Medicine ID</th><th>Frequency</th><th>Days</th></tr>");

        for (Map<String, Object> med : prescription.getMedicines()) {

            html.append("<tr>");

            html.append("<td>")
                    .append(med.get("medicineId"))
                    .append("</td>");

            html.append("<td>")
                    .append(med.get("frequency"))
                    .append("</td>");

            html.append("<td>")
                    .append(med.get("days"))
                    .append("</td>");

            html.append("</tr>");
        }

        html.append("</table>");

        html.append("<h2>Lab Tests</h2>");
        html.append("<ul>");

        for (Map<String, Object> test : prescription.getLabTests()) {

            html.append("<li>")
                    .append(test.get("labTestId"))
                    .append("</li>");
        }

        html.append("</ul>");

        html.append("</body></html>");

        return html.toString();
    }
}