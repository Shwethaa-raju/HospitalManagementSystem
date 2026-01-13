package client;

import dto.PatientDTO;
import org.springframework.web.client.RestTemplate;

import java.util.UUID;

public class PatientClient {

    private final RestTemplate restTemplate;

    public PatientClient(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public PatientDTO getPatient(UUID patientId) {
        return restTemplate.getForObject(
                "http://patient-service/internal/patients/" + patientId,
                PatientDTO.class
        );
    }
}
