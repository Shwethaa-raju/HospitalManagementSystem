CREATE TABLE notification (
                              id UUID PRIMARY KEY,
                              reference_id UUID NOT NULL,     -- billId
                              patient_id UUID NOT NULL,
                              channel VARCHAR(20) NOT NULL,   -- WHATSAPP, EMAIL
                              status VARCHAR(20) NOT NULL,    -- SENT, FAILED
                              error_message TEXT,
                              created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
