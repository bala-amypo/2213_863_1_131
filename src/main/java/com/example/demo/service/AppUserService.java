package com.example.demo.service;

import com.example.demo.entity.AppUser;

public interface AppUserService {
    AppUser register(AppUser appUser);
    AppUser login(String email, String password);
}
