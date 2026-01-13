package com.hms.doctorservice.grpc;

import com.hms.doctorservice.service.DoctorService;
import doctor.DoctorScheduleRequest;
import doctor.DoctorScheduleResponse;
import doctor.DoctorServiceGrpc;

import io.grpc.stub.StreamObserver;
import net.devh.boot.grpc.server.service.GrpcService;

import java.time.LocalDate;
import java.util.List;


import java.time.format.DateTimeFormatter;
//import java.time.format.DateTimeParseException;


@GrpcService
public class DoctorGrpcService extends DoctorServiceGrpc.DoctorServiceImplBase {
    private final DoctorService doctorService;
    DoctorGrpcService(DoctorService doctorService){
        this.doctorService = doctorService;
    }

    private doctor.Slot toProtoSlot(com.hms.doctorservice.dto.Slot domain) {
        return doctor.Slot.newBuilder()
                .setDoctorId(domain.getDoctorId().toString())
                .setStartTime( domain.getStartTime().toString())
                .setEndTime( domain.getEndTime().toString())
                .build();
    }

    @Override
    public void getDoctorSchedule( DoctorScheduleRequest request, StreamObserver<DoctorScheduleResponse> responseObserver) {

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        LocalDate date = LocalDate.parse(request.getDate(), formatter);

        // call your domain/service layer
        List<com.hms.doctorservice.dto.Slot> slots = doctorService.getAllSlots( request.getSpeciality(), date );

        DoctorScheduleResponse response =
                DoctorScheduleResponse.newBuilder()
                        .addAllSlots(
                                slots.stream()
                                        .map(this::toProtoSlot)
                                        .toList()
                        )
                        .build();

        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

}
