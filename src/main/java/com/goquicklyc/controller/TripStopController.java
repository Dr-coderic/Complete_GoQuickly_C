package com.goquicklyc.controller;

import com.goquicklyc.model.TripStop;
import com.goquicklyc.service.TripStopService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/trip-stops")
public class TripStopController {
    @Autowired
    private TripStopService tripStopService;

    @GetMapping
    public List<TripStop> getAllTripStops() {
        return tripStopService.getAllTripStops();
    }

    @GetMapping("/{id}")
    public ResponseEntity<TripStop> getTripStopById(@PathVariable Long id) {
        Optional<TripStop> stop = tripStopService.getTripStopById(id);
        return stop.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/trip/{tripId}")
    public List<TripStop> getTripStopsByTripId(@PathVariable Long tripId) {
        return tripStopService.getTripStopsByTripId(tripId);
    }

    @PostMapping
    public TripStop createTripStop(@RequestBody TripStop tripStop) {
        return tripStopService.saveTripStop(tripStop);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTripStop(@PathVariable Long id) {
        tripStopService.deleteTripStop(id);
        return ResponseEntity.ok().build();
    }
} 