package com.ecentum.MonthlyExpenses.web.controller;

import com.ecentum.MonthlyExpenses.entity.User;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@RestController
public class UserController {

    @GetMapping("/{id}")
    public String index(@PathVariable Long id, Model model) {
        // Use the 'id' parameter in your logic if needed
        model.addAttribute("userId", id);
        return "redirect:/index";

    }

    @PostMapping("/register")
    public String userRegistration(@ModelAttribute User user, Model model) {
        System.out.println(user.toString());
        System.out.println(user.getFname());
        System.out.println(user.getLname());
        System.out.println(user.getEmail());
        model.addAttribute("firstname", user.getFname());
        model.addAttribute("lastname", user.getLname());
        return "redirect:/welcome";
    }
}

