package com.goquicklyc.service;

import com.goquicklyc.model.Booking;
import com.goquicklyc.repository.BookingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class BookingService {
    @Autowired
    private BookingRepository bookingRepository;

    public List<Booking> getAllBookings() {
        return bookingRepository.findAll();
    }

    public Optional<Booking> getBookingById(Long id) {
        return bookingRepository.findById(id);
    }

    public List<Booking> getBookingsByTripId(Long tripId) {
        return bookingRepository.findByTripId(tripId);
    }

    public List<Booking> getBookingsByPassengerId(Long passengerId) {
        return bookingRepository.findByPassengerId(passengerId);
    }

    public Booking saveBooking(Booking booking) {
        return bookingRepository.save(booking);
    }

    public void updateBookingStatus(Long id, String status) {
        bookingRepository.findById(id).ifPresent(booking -> {
            booking.setStatus(com.goquicklyc.model.BookingStatus.valueOf(status));
            bookingRepository.save(booking);
        });
    }
} 