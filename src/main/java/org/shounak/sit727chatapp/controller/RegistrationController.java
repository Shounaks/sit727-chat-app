package org.shounak.sit727chatapp.controller;

import org.shounak.sit727chatapp.model.Data;
import org.shounak.sit727chatapp.model.LoginRequest;
import org.shounak.sit727chatapp.repository.DataRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class RegistrationController {

    private final DataRepository repository;
    private final PasswordEncoder passwordEncoder;

    public RegistrationController(DataRepository repository, PasswordEncoder passwordEncoder) {
        this.repository = repository;
        this.passwordEncoder = passwordEncoder;
    }


    @GetMapping("/register")
    public String showRegistrationForm(Model model) {
        model.addAttribute("user", new LoginRequest());
        return "register";
    }

    @PostMapping("/register")
    public String registerUser(@ModelAttribute("user") LoginRequest registrationDTO, @RequestParam(required = false) String errorMessage, Model model) {
        if (repository.findByUsername(registrationDTO.getUsername()).isEmpty()) {
            this.register(registrationDTO);
            return "redirect:/login?registered";
        }
        model.addAttribute("errorMessage", "Username Already Present");
        return "register";
    }

    public void register(LoginRequest registrationRequest) {
        Data user = new Data();
        user.setUsername(registrationRequest.getUsername());
//        user.setEmail(registrationRequest.getUsername());
        user.setPassword(passwordEncoder.encode(registrationRequest.getPassword())); // Encrypt password
        repository.save(user);
    }
}