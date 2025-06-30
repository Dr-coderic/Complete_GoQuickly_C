package com.goquicklyc.controller;

import com.goquicklyc.model.User;
import com.goquicklyc.model.Vehicle;
import com.goquicklyc.service.UserService;
import com.goquicklyc.service.VehicleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@Controller
@RequestMapping("/vehicles")
public class VehicleManageController {
    @Autowired
    private VehicleService vehicleService;
    @Autowired
    private UserService userService;

    @GetMapping
    public String manageVehicles(Authentication authentication, Model model) {
        String email = authentication.getName();
        User user = userService.getUserByEmail(email).orElse(null);
        List<Vehicle> vehicles = vehicleService.getVehiclesByOwnerId(user.getId());
        model.addAttribute("vehicles", vehicles);
        return "vehicle_manage";
    }

    @PostMapping
    public String addVehicle(@RequestParam String make,
                             @RequestParam String modelName,
                             @RequestParam Integer year,
                             @RequestParam String licensePlate,
                             @RequestParam Integer capacity,
                             Authentication authentication) {
        String email = authentication.getName();
        User user = userService.getUserByEmail(email).orElse(null);
        if (user != null) {
            Vehicle vehicle = new Vehicle();
            vehicle.setOwner(user);
            vehicle.setMake(make);
            vehicle.setModel(modelName);
            vehicle.setYear(year);
            vehicle.setLicensePlate(licensePlate);
            vehicle.setCapacity(capacity);
            vehicle.setEnabled(true);
            vehicleService.saveVehicle(vehicle);
        }
        return "redirect:/vehicles";
    }

    @PostMapping("/{id}/toggle")
    public String toggleVehicleStatus(@PathVariable Long id, Authentication authentication) {
        String email = authentication.getName();
        User user = userService.getUserByEmail(email).orElse(null);
        Vehicle vehicle = vehicleService.getVehicleById(id).orElse(null);
        if (user != null && vehicle != null && vehicle.getOwner().getId().equals(user.getId())) {
            vehicle.setEnabled(!vehicle.isEnabled());
            vehicleService.saveVehicle(vehicle);
        }
        return "redirect:/vehicles";
    }
} 