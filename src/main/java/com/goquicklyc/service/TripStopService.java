package com.goquicklyc.service;

import com.goquicklyc.model.TripStop;
import com.goquicklyc.repository.TripStopRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class TripStopService {
    @Autowired
    private TripStopRepository tripStopRepository;

    public List<TripStop> getAllTripStops() {
        return tripStopRepository.findAll();
    }

    public Optional<TripStop> getTripStopById(Long id) {
        return tripStopRepository.findById(id);
    }

    public List<TripStop> getTripStopsByTripId(Long tripId) {
        return tripStopRepository.findByTripIdOrderByStopOrderAsc(tripId);
    }

    public TripStop saveTripStop(TripStop tripStop) {
        return tripStopRepository.save(tripStop);
    }

    public void deleteTripStop(Long id) {
        tripStopRepository.deleteById(id);
    }
} 