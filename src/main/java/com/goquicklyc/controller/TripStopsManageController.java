package com.goquicklyc.controller;

import com.goquicklyc.model.Trip;
import com.goquicklyc.model.TripStop;
import com.goquicklyc.model.User;
import com.goquicklyc.service.TripService;
import com.goquicklyc.service.TripStopService;
import com.goquicklyc.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDateTime;
import java.util.List;

@Controller
@RequestMapping("/trips/manage/{tripId}/stops")
public class TripStopsManageController {
    @Autowired
    private TripService tripService;
    @Autowired
    private TripStopService tripStopService;
    @Autowired
    private UserService userService;

    @GetMapping
    public String manageStops(@PathVariable Long tripId, Authentication authentication, Model model) {
        String email = authentication.getName();
        User user = userService.getUserByEmail(email).orElse(null);
        Trip trip = tripService.getTripById(tripId).orElse(null);
        if (user == null || trip == null || !trip.getVehicle().getOwner().getId().equals(user.getId())) {
            return "redirect:/trips/manage";
        }
        List<TripStop> stops = tripStopService.getTripStopsByTripId(tripId);
        model.addAttribute("trip", trip);
        model.addAttribute("stops", stops);
        return "trip_stops_manage";
    }

    @PostMapping
    public String addStop(@PathVariable Long tripId,
                          @RequestParam String location,
                          @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime estimatedArrivalTime,
                          @RequestParam Integer stopOrder,
                          Authentication authentication) {
        String email = authentication.getName();
        User user = userService.getUserByEmail(email).orElse(null);
        Trip trip = tripService.getTripById(tripId).orElse(null);
        if (user != null && trip != null && trip.getVehicle().getOwner().getId().equals(user.getId())) {
            TripStop stop = new TripStop();
            stop.setTrip(trip);
            stop.setLocation(location);
            stop.setEstimatedArrivalTime(estimatedArrivalTime);
            stop.setStopOrder(stopOrder);
            tripStopService.saveTripStop(stop);
        }
        return "redirect:/trips/manage/" + tripId + "/stops";
    }

    @PostMapping("/{stopId}/delete")
    public String deleteStop(@PathVariable Long tripId, @PathVariable Long stopId, Authentication authentication) {
        String email = authentication.getName();
        User user = userService.getUserByEmail(email).orElse(null);
        Trip trip = tripService.getTripById(tripId).orElse(null);
        TripStop stop = tripStopService.getTripStopById(stopId).orElse(null);
        if (user != null && trip != null && stop != null && trip.getVehicle().getOwner().getId().equals(user.getId()) && stop.getTrip().getId().equals(tripId)) {
            tripStopService.deleteTripStop(stopId);
        }
        return "redirect:/trips/manage/" + tripId + "/stops";
    }
} 