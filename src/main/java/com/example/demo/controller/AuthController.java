package com.example.demo.controller;

import com.example.demo.entity.AppUser;
import com.example.demo.service.AppUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AppUserService appUserService;

    @PostMapping("/register")
    public AppUser register(@RequestBody AppUser appUser) {
        return appUserService.register(appUser);
    }

    @PostMapping("/login")
    public AppUser login(@RequestParam String email, @RequestParam String password) {
        return appUserService.login(email, password);
    }
}
