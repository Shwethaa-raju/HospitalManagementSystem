package com.hms.prescriptionservice.repository;

import com.hms.prescriptionservice.model.Prescription;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface PrescriptionRepository extends MongoRepository<Prescription, ObjectId> {

}
