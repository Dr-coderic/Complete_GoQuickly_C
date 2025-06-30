package com.goquicklyc.repository;

import com.goquicklyc.model.TripStop;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface TripStopRepository extends JpaRepository<TripStop, Long> {
    List<TripStop> findByTripIdOrderByStopOrderAsc(Long tripId);
} 