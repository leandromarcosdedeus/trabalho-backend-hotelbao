package com.hotelbao.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.beans.factory.annotation.Autowired;
import com.hotelbao.repository.StayRepository;
import com.hotelbao.repository.RoomRepository;
import com.hotelbao.repository.UserRepository;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.security.access.prepost.PreAuthorize;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("/api/test")
public class TestController {
    @Autowired StayRepository stayRepository;
    @Autowired RoomRepository roomRepository;
    @Autowired UserRepository userRepository;

    @GetMapping("/user")
    public String userAccess() {
        return "user";
    }
    @GetMapping("/admin")
    public String adminAccess() {
        return "admin";
    }

    @DeleteMapping("/clear-all")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public String clearAll() {
        stayRepository.deleteAll();
        roomRepository.deleteAll();
        userRepository.deleteAll();
        return "Tabelas stays, rooms e users limpas com sucesso.";
    }
} 