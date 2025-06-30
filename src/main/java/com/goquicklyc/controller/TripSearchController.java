package com.goquicklyc.controller;

import com.goquicklyc.model.Booking;
import com.goquicklyc.model.Trip;
import com.goquicklyc.model.TripStop;
import com.goquicklyc.model.User;
import com.goquicklyc.service.BookingService;
import com.goquicklyc.service.TripService;
import com.goquicklyc.service.TripStopService;
import com.goquicklyc.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/trips")
public class TripSearchController {
    @Autowired
    private TripService tripService;
    @Autowired
    private TripStopService tripStopService;
    @Autowired
    private UserService userService;
    @Autowired
    private BookingService bookingService;

    @GetMapping("/search")
    public String searchTrips(@RequestParam(required = false) String from,
                             @RequestParam(required = false) String to,
                             @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
                             Model model) {
        model.addAttribute("from", from);
        model.addAttribute("to", to);
        model.addAttribute("date", date);
        Map<Long, List<TripStop>> tripStopsMap = new HashMap<>();
        Map<Long, Integer> segmentSeatsMap = new HashMap<>();
        List<Trip> filtered = new ArrayList<>();
        if (from != null && to != null && date != null) {
            List<Trip> allTrips = tripService.getAllTrips();
            for (Trip trip : allTrips) {
                if (!trip.isActive() || trip.getAvailableSeats() <= 0) continue;
                if (!trip.getDepartureTime().toLocalDate().equals(date)) continue;
                // Build ordered list of all stops: start, intermediate, end
                List<String> locations = new ArrayList<>();
                locations.add(trip.getStartLocation());
                List<TripStop> stops = tripStopService.getTripStopsByTripId(trip.getId())
                        .stream().sorted(Comparator.comparingInt(TripStop::getStopOrder)).collect(Collectors.toList());
                for (TripStop stop : stops) {
                    locations.add(stop.getLocation());
                }
                locations.add(trip.getEndLocation());
                tripStopsMap.put(trip.getId(), stops);
                int fromIdx = locations.stream().map(String::toLowerCase).collect(Collectors.toList()).indexOf(from.toLowerCase());
                int toIdx = locations.stream().map(String::toLowerCase).collect(Collectors.toList()).indexOf(to.toLowerCase());
                if (fromIdx != -1 && toIdx != -1 && fromIdx < toIdx) {
                    // Check segment seat availability
                    int minAvailable = Integer.MAX_VALUE;
                    List<Booking> bookings = bookingService.getBookingsByTripId(trip.getId());
                    for (int i = fromIdx; i < toIdx; i++) {
                        int bookedSeats = 0;
                        for (Booking b : bookings) {
                            int bFromIdx = locations.stream().map(String::toLowerCase).collect(Collectors.toList()).indexOf(b.getPickupLocation().toLowerCase());
                            int bToIdx = locations.stream().map(String::toLowerCase).collect(Collectors.toList()).indexOf(b.getDropoffLocation().toLowerCase());
                            if (bFromIdx <= i && bToIdx > i) {
                                bookedSeats += b.getNumberOfSeats();
                            }
                        }
                        int available = trip.getAvailableSeats() - bookedSeats;
                        if (available < minAvailable) minAvailable = available;
                    }
                    if (minAvailable > 0) {
                        filtered.add(trip);
                        segmentSeatsMap.put(trip.getId(), minAvailable);
                    }
                }
            }
        }
        model.addAttribute("trips", filtered);
        model.addAttribute("tripStopsMap", tripStopsMap);
        model.addAttribute("segmentSeatsMap", segmentSeatsMap);
        return "trip_search";
    }

    @PostMapping("/bookings")
    public String bookTrip(@RequestParam Long tripId,
                           @RequestParam Integer numberOfSeats,
                           @RequestParam String pickupLocation,
                           @RequestParam String dropoffLocation,
                           Authentication authentication) {
        String email = authentication.getName();
        User user = userService.getUserByEmail(email).orElse(null);
        Trip trip = tripService.getTripById(tripId).orElse(null);
        if (user != null && trip != null && numberOfSeats > 0) {
            Booking booking = new Booking();
            booking.setTrip(trip);
            booking.setPassenger(user);
            booking.setPickupLocation(pickupLocation);
            booking.setDropoffLocation(dropoffLocation);
            booking.setNumberOfSeats(numberOfSeats);
            booking.setTotalPrice(trip.getPrice() * numberOfSeats);
            bookingService.saveBooking(booking);
            // Optionally update trip's availableSeats if you want to keep it as the global min
        }
        return "redirect:/trips/search";
    }
} 