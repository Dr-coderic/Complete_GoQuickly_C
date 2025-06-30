package com.goquicklyc.controller;

import com.goquicklyc.model.Vehicle;
import com.goquicklyc.service.VehicleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/vehicles")
public class VehicleController {
    @Autowired
    private VehicleService vehicleService;

    @GetMapping
    public List<Vehicle> getAllVehicles() {
        return vehicleService.getAllVehicles();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Vehicle> getVehicleById(@PathVariable Long id) {
        Optional<Vehicle> vehicle = vehicleService.getVehicleById(id);
        return vehicle.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/owner/{ownerId}")
    public List<Vehicle> getVehiclesByOwnerId(@PathVariable Long ownerId) {
        return vehicleService.getVehiclesByOwnerId(ownerId);
    }

    @PostMapping
    public Vehicle createVehicle(@RequestBody Vehicle vehicle) {
        return vehicleService.saveVehicle(vehicle);
    }

    @PutMapping("/{id}/disable")
    public ResponseEntity<Void> disableVehicle(@PathVariable Long id) {
        vehicleService.disableVehicle(id);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{id}/enable")
    public ResponseEntity<Void> enableVehicle(@PathVariable Long id) {
        vehicleService.enableVehicle(id);
        return ResponseEntity.ok().build();
    }
} 