package com.goquicklyc.controller;

import com.goquicklyc.model.Vehicle;
import com.goquicklyc.model.VerificationStatus;
import com.goquicklyc.service.VehicleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/admin/vehicles")
public class AdminVehicleController {
    @Autowired
    private VehicleService vehicleService;

    @GetMapping("/verify")
    public String listVehiclesForVerification(Model model) {
        List<Vehicle> vehicles = vehicleService.getAllVehicles().stream()
                .filter(v -> v.getVerificationStatus() == VerificationStatus.PENDING_VERIFICATION)
                .collect(Collectors.toList());
        model.addAttribute("vehicles", vehicles);
        return "admin_vehicle_verification_list";
    }

    @GetMapping("/{id}/verify")
    public String verifyVehicleDetails(@PathVariable Long id, Model model) {
        Vehicle vehicle = vehicleService.getVehicleById(id).orElseThrow();
        model.addAttribute("vehicle", vehicle);
        return "admin_vehicle_verify_details";
    }

    @PostMapping("/{id}/verify")
    public String processVerification(@PathVariable Long id, @RequestParam VerificationStatus status) {
        Vehicle vehicle = vehicleService.getVehicleById(id).orElseThrow();
        vehicle.setVerificationStatus(status);
        vehicleService.saveVehicle(vehicle);
        return "redirect:/admin/vehicles/verify";
    }
} 