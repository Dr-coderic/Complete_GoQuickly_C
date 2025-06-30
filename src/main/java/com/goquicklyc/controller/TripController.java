package com.goquicklyc.controller;

import com.goquicklyc.model.Trip;
import com.goquicklyc.service.TripService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/trips")
public class TripController {
    @Autowired
    private TripService tripService;

    @GetMapping
    public List<Trip> getAllTrips() {
        return tripService.getAllTrips();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Trip> getTripById(@PathVariable Long id) {
        Optional<Trip> trip = tripService.getTripById(id);
        return trip.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/vehicle/{vehicleId}")
    public List<Trip> getTripsByVehicleId(@PathVariable Long vehicleId) {
        return tripService.getTripsByVehicleId(vehicleId);
    }

    @PostMapping
    public Trip createTrip(@RequestBody Trip trip) {
        return tripService.saveTrip(trip);
    }

    @PutMapping("/{id}/deactivate")
    public ResponseEntity<Void> deactivateTrip(@PathVariable Long id) {
        tripService.deactivateTrip(id);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{id}/activate")
    public ResponseEntity<Void> activateTrip(@PathVariable Long id) {
        tripService.activateTrip(id);
        return ResponseEntity.ok().build();
    }
} 