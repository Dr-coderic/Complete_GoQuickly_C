package com.goquicklyc.controller;

import com.goquicklyc.model.Trip;
import com.goquicklyc.model.User;
import com.goquicklyc.model.Vehicle;
import com.goquicklyc.service.TripService;
import com.goquicklyc.service.UserService;
import com.goquicklyc.service.VehicleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDateTime;
import java.util.List;

@Controller
@RequestMapping("/trips/manage")
public class TripManageController {
    @Autowired
    private TripService tripService;
    @Autowired
    private VehicleService vehicleService;
    @Autowired
    private UserService userService;

    @GetMapping
    public String manageTrips(Authentication authentication, Model model) {
        String email = authentication.getName();
        User user = userService.getUserByEmail(email).orElse(null);
        List<Vehicle> vehicles = vehicleService.getVehiclesByOwnerId(user.getId());
        List<Trip> trips = tripService.getAllTrips().stream()
                .filter(t -> t.getVehicle().getOwner().getId().equals(user.getId()))
                .toList();
        model.addAttribute("vehicles", vehicles);
        model.addAttribute("trips", trips);
        return "trip_manage";
    }

    @PostMapping
    public String createTrip(@RequestParam Long vehicleId,
                             @RequestParam String startLocation,
                             @RequestParam String endLocation,
                             @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime departureTime,
                             @RequestParam Double price,
                             @RequestParam Integer availableSeats,
                             Authentication authentication) {
        String email = authentication.getName();
        User user = userService.getUserByEmail(email).orElse(null);
        Vehicle vehicle = vehicleService.getVehicleById(vehicleId).orElse(null);
        if (user != null && vehicle != null && vehicle.getOwner().getId().equals(user.getId())) {
            Trip trip = new Trip();
            trip.setVehicle(vehicle);
            trip.setStartLocation(startLocation);
            trip.setEndLocation(endLocation);
            trip.setDepartureTime(departureTime);
            trip.setPrice(price);
            trip.setAvailableSeats(availableSeats);
            trip.setActive(true);
            tripService.saveTrip(trip);
        }
        return "redirect:/trips/manage";
    }

    @PostMapping("/{id}/toggle")
    public String toggleTripStatus(@PathVariable Long id, Authentication authentication) {
        String email = authentication.getName();
        User user = userService.getUserByEmail(email).orElse(null);
        Trip trip = tripService.getTripById(id).orElse(null);
        if (user != null && trip != null && trip.getVehicle().getOwner().getId().equals(user.getId())) {
            trip.setActive(!trip.isActive());
            tripService.saveTrip(trip);
        }
        return "redirect:/trips/manage";
    }
} 