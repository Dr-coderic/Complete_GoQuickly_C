package com.goquicklyc.controller;

import com.goquicklyc.model.*;
import com.goquicklyc.service.FileStorageService;
import com.goquicklyc.service.VehicleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.util.Set;

@Controller
@RequestMapping("/vehicles/{id}/documents")
public class VehicleDocumentController {
    @Autowired
    private VehicleService vehicleService;
    @Autowired
    private FileStorageService fileStorageService;

    @GetMapping
    public String viewDocuments(@PathVariable Long id, Model model) {
        Vehicle vehicle = vehicleService.getVehicleById(id).orElseThrow();
        model.addAttribute("vehicle", vehicle);
        model.addAttribute("documents", vehicle.getDocuments());
        return "vehicle_documents";
    }

    @PostMapping
    public String uploadDocuments(@PathVariable Long id,
                                 @RequestParam(required = false) MultipartFile drivingLicense,
                                 @RequestParam(required = false) MultipartFile carImage,
                                 @RequestParam(required = false) MultipartFile rc,
                                 @RequestParam(required = false) MultipartFile selfImage,
                                 @RequestParam(required = false) MultipartFile aadhar) {
        Vehicle vehicle = vehicleService.getVehicleById(id).orElseThrow();
        Set<VehicleDocument> documents = vehicle.getDocuments();
        if (drivingLicense != null && !drivingLicense.isEmpty()) {
            String fileName = fileStorageService.storeFile(drivingLicense);
            documents.add(new VehicleDocument(null, vehicle, DocumentType.DRIVING_LICENSE, fileName, null));
        }
        if (carImage != null && !carImage.isEmpty()) {
            String fileName = fileStorageService.storeFile(carImage);
            documents.add(new VehicleDocument(null, vehicle, DocumentType.CAR_IMAGE, fileName, null));
        }
        if (rc != null && !rc.isEmpty()) {
            String fileName = fileStorageService.storeFile(rc);
            documents.add(new VehicleDocument(null, vehicle, DocumentType.REGISTRATION_CERTIFICATE, fileName, null));
        }
        if (selfImage != null && !selfImage.isEmpty()) {
            String fileName = fileStorageService.storeFile(selfImage);
            documents.add(new VehicleDocument(null, vehicle, DocumentType.SELF_IMAGE, fileName, null));
        }
        if (aadhar != null && !aadhar.isEmpty()) {
            String fileName = fileStorageService.storeFile(aadhar);
            documents.add(new VehicleDocument(null, vehicle, DocumentType.AADHAR_CARD, fileName, null));
        }
        vehicle.setDocuments(documents);
        vehicle.setVerificationStatus(VerificationStatus.PENDING_VERIFICATION);
        vehicleService.saveVehicle(vehicle);
        return "redirect:/vehicles/" + id + "/documents";
    }
} 