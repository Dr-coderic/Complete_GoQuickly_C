package com.goquicklyc.repository;

import com.goquicklyc.model.Trip;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface TripRepository extends JpaRepository<Trip, Long> {
    List<Trip> findByVehicleId(Long vehicleId);
} 