package com.example.demo.service.impl;

import com.example.demo.entity.AppUser;
import com.example.demo.exception.BadRequestException;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.repository.AppUserRepository;
import com.example.demo.service.AppUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AppUserServiceImpl implements AppUserService {

    private final AppUserRepository appUserRepository;

    @Override
    public AppUser register(AppUser appUser) {
        if (appUserRepository.findByEmail(appUser.getEmail()).isPresent()) {
            throw new BadRequestException("unique");
        }
        return appUserRepository.save(appUser);
    }

    @Override
    public AppUser login(String email, String password) {
        AppUser user = appUserRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
                
        if (!user.getPassword().equals(password)) {
            throw new BadRequestException("Invalid credentials");
        }
        return user;
    }
}
