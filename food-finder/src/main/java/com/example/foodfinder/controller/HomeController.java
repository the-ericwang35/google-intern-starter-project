package com.example.foodfinder.controller;

import com.example.foodfinder.models.Form;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {
    @GetMapping("/")
    public String home(Model model) {
        model.addAttribute("form", new Form());
        return "index.html";
    }
}