package com.goquicklyc.controller;

import com.goquicklyc.model.Booking;
import com.goquicklyc.model.User;
import com.goquicklyc.service.BookingService;
import com.goquicklyc.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import java.util.List;

@Controller
@RequestMapping("/bookings")
public class BookingHistoryController {
    @Autowired
    private BookingService bookingService;
    @Autowired
    private UserService userService;

    @GetMapping
    public String bookingHistory(Authentication authentication, Model model) {
        String email = authentication.getName();
        User user = userService.getUserByEmail(email).orElse(null);
        List<Booking> bookings = bookingService.getBookingsByPassengerId(user.getId());
        model.addAttribute("bookings", bookings);
        return "booking_history";
    }
} 