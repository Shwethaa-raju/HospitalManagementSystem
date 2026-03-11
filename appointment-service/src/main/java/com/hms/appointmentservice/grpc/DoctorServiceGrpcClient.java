package com.hms.appointmentservice.grpc;

import doctor.DoctorScheduleRequest;
import doctor.DoctorSlotRequest;
import doctor.DoctorScheduleResponse;
import doctor.DoctorServiceGrpc;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

@Service
public class DoctorServiceGrpcClient {
    private static final Logger log = LoggerFactory.getLogger(DoctorServiceGrpcClient.class);
    private final DoctorServiceGrpc.DoctorServiceBlockingStub blockingStub;

    public DoctorServiceGrpcClient(
            @Value("${doctor.service.address:localhost}") String serverAddress,
            @Value("${doctor.service.grpc.port:9001}") int serverPort
    ){

        log.info("Connecting to doctor service at {}:{}", serverAddress, serverPort);
        ManagedChannel channel = ManagedChannelBuilder.forAddress(serverAddress, serverPort).usePlaintext().build();
        blockingStub = DoctorServiceGrpc.newBlockingStub(channel);
    }

    public DoctorScheduleResponse getAllSlots(String speciality, String date){
        DoctorScheduleRequest request = DoctorScheduleRequest.newBuilder().setSpeciality(speciality).setDate(date).build();

        DoctorScheduleResponse response = blockingStub.getDoctorSchedule(request);
        return response;
    }

    public DoctorScheduleResponse getDoctorSlots(UUID doctorId, LocalDate date){
        DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        String dateStr = date.format(DATE_FORMATTER);
        DoctorSlotRequest request =  DoctorSlotRequest.newBuilder().setDoctorId(doctorId.toString()).setDate(dateStr).build();

        DoctorScheduleResponse response = blockingStub.getDoctorSlots(request);
        return response;
    }

}
