package com.example.demo.service;

import com.example.demo.entity.AppUser;

public interface AppUserService {
    AppUser registerUser(AppUser user);
    AppUser findByEmail(String email);
}
