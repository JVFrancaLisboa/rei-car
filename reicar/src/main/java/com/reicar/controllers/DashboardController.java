package com.reicar.controllers;

import com.reicar.repositories.ServiceOrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
public class DashboardController {

    private final ServiceOrderRepository repository;

    @GetMapping("/dashboard")
    public String showDashboard(Model model) {
        model.addAttribute("orders", repository.findAll());
        return "screens/dashboard";
    }
}
