package com.goquicklyc.controller;

import com.goquicklyc.model.Booking;
import com.goquicklyc.service.BookingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/bookings")
public class BookingController {
    @Autowired
    private BookingService bookingService;

    @GetMapping
    public List<Booking> getAllBookings() {
        return bookingService.getAllBookings();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Booking> getBookingById(@PathVariable Long id) {
        Optional<Booking> booking = bookingService.getBookingById(id);
        return booking.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/trip/{tripId}")
    public List<Booking> getBookingsByTripId(@PathVariable Long tripId) {
        return bookingService.getBookingsByTripId(tripId);
    }

    @GetMapping("/passenger/{passengerId}")
    public List<Booking> getBookingsByPassengerId(@PathVariable Long passengerId) {
        return bookingService.getBookingsByPassengerId(passengerId);
    }

    @PostMapping
    public Booking createBooking(@RequestBody Booking booking) {
        return bookingService.saveBooking(booking);
    }

    @PutMapping("/{id}/status")
    public ResponseEntity<Void> updateBookingStatus(@PathVariable Long id, @RequestParam String status) {
        bookingService.updateBookingStatus(id, status);
        return ResponseEntity.ok().build();
    }
} 