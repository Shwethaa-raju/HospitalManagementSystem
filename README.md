```mermaid
flowchart TB

%% SERVICES
A[Appointment Service]
D[Doctor Service]
P[Prescription Service]
B[Billing Service]
PDF[PDF Service]
N[Notification Service]
S3[(AWS S3 Bucket)]

%% RELATIONSHIPS
A -- "gRPC: Get Available Slots" --> D
A -- "Kafka: APPOINTMENT_BOOKED" --> P
P -- "Kafka: PRESCRIPTION_CREATED" --> B

P -- "gRPC: Generate Prescription PDF" --> PDF
B -- "gRPC: Generate Bill PDF" --> PDF

PDF --> S3

PDF -- "Kafka: PRESCRIPTION_PDF_GENERATED" --> N
PDF -- "Kafka: BILL_PDF_GENERATED" --> N


%% COLORS
style A fill:#4CAF50,color:#fff,stroke:#2E7D32,stroke-width:2px
style D fill:#81C784,color:#000,stroke:#2E7D32,stroke-width:2px

style P fill:#64B5F6,color:#000,stroke:#1565C0,stroke-width:2px
style B fill:#64B5F6,color:#000,stroke:#1565C0,stroke-width:2px

style PDF fill:#FFB74D,color:#000,stroke:#EF6C00,stroke-width:2px

style N fill:#BA68C8,color:#fff,stroke:#6A1B9A,stroke-width:2px

style S3 fill:#FFD54F,color:#000,stroke:#F57F17,stroke-width:2px
```
# Microservices Overview

A backend Hospital Management System that orchestrates the full patient workflow—from doctor appointment booking to prescription generation, billing, PDF document creation, and patient notifications using an event-driven microservices architecture.

Key backend design considerations include:
* Preventing double booking of appointment slots using Redis-based temporary locks
* Event-driven state propagation across services using Kafka
* Clear separation of domain responsibilities (appointments, prescriptions, billing, documents, notifications)
* Synchronous service communication via gRPC for real-time data (e.g., doctor slot availability)
* Asynchronous processing pipelines for prescription creation, billing, PDF generation, and notifications.
  
Core Technologies Used : Java 17, Spring Boot, Apache Kafka, gRPC, MongoDB, Postgres SQL, Redis, Docker, AWS S3, Maven

# Core Business Services

### Appointment Service

Handles the appointment booking workflow. This service contains the most critical booking logic and race-condition handling.

* Fetch available slots from Doctor Service (gRPC)
* Validate slot availability
* Persist confirmed appointments
* Publish APPOINTMENT_BOOKED Kafka event
* Prevent double booking using Redis temporary slot locks

### Prescription Service 

Responsible for creating medical prescriptions after appointments.

* Consume APPOINTMENT_BOOKED event
* Generate and store prescription records
* Request prescription PDF generation via PDF Service (gRPC)
* Publish PRESCRIPTION_CREATED Kafka event

### Billing Service

Manages bill creation for medical services.

* Consume PRESCRIPTION_CREATED event
* Generate billing records
* Calculate consultation charges
* Request bill PDF generation from PDF Service (gRPC)
* Represents the financial processing layer of the system.

# Supporting Infrastructure Services

### PDF Service

* Handles document generation and storage.
* Generate PDFs for prescriptions and bills
* Upload documents to AWS S3
* Publish kafka events (PRESCRIPTION_PDF_GENERATED, BILL_PDF_GENERATED) when documents are generated

### Notification Service

Responsible for patient notifications.

* Consume PDF generation events
* Send email and WhatsApp notifications which delivers links to generated documents
* Includes retry logic for failed notifications.

### Doctor Service

* Provides doctor availability and appointment slots.
* Store doctor schedules
* Dynamically generate appointment slots
* Expose slot availability via gRPC API
