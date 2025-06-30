package com.goquicklyc.service;

import com.goquicklyc.model.Vehicle;
import com.goquicklyc.repository.VehicleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class VehicleService {
    @Autowired
    private VehicleRepository vehicleRepository;

    public List<Vehicle> getAllVehicles() {
        return vehicleRepository.findAll();
    }

    public Optional<Vehicle> getVehicleById(Long id) {
        return vehicleRepository.findById(id);
    }

    public List<Vehicle> getVehiclesByOwnerId(Long ownerId) {
        return vehicleRepository.findByOwnerId(ownerId);
    }

    public Vehicle saveVehicle(Vehicle vehicle) {
        return vehicleRepository.save(vehicle);
    }

    public void disableVehicle(Long id) {
        vehicleRepository.findById(id).ifPresent(vehicle -> {
            vehicle.setEnabled(false);
            vehicleRepository.save(vehicle);
        });
    }

    public void enableVehicle(Long id) {
        vehicleRepository.findById(id).ifPresent(vehicle -> {
            vehicle.setEnabled(true);
            vehicleRepository.save(vehicle);
        });
    }
} 