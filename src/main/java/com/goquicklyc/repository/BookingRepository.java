package com.goquicklyc.repository;

import com.goquicklyc.model.Booking;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface BookingRepository extends JpaRepository<Booking, Long> {
    List<Booking> findByTripId(Long tripId);
    List<Booking> findByPassengerId(Long passengerId);
} 