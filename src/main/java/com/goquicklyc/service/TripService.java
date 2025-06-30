package com.goquicklyc.service;

import com.goquicklyc.model.Trip;
import com.goquicklyc.repository.TripRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class TripService {
    @Autowired
    private TripRepository tripRepository;

    public List<Trip> getAllTrips() {
        return tripRepository.findAll();
    }

    public Optional<Trip> getTripById(Long id) {
        return tripRepository.findById(id);
    }

    public List<Trip> getTripsByVehicleId(Long vehicleId) {
        return tripRepository.findByVehicleId(vehicleId);
    }

    public Trip saveTrip(Trip trip) {
        return tripRepository.save(trip);
    }

    public void deactivateTrip(Long id) {
        tripRepository.findById(id).ifPresent(trip -> {
            trip.setActive(false);
            tripRepository.save(trip);
        });
    }

    public void activateTrip(Long id) {
        tripRepository.findById(id).ifPresent(trip -> {
            trip.setActive(true);
            tripRepository.save(trip);
        });
    }
} 