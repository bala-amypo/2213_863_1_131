package com.example.demo.service.impl;

import org.springframework.stereotype.Service;
import com.example.demo.repository.AppUserRepository;
import com.example.demo.service.AppUserService;

@Service
public class AppUserServiceImpl implements AppUserService {

    private final AppUserRepository appUserRepository;

    public AppUserServiceImpl(AppUserRepository appUserRepository) {
        this.appUserRepository = appUserRepository;
    }
}
