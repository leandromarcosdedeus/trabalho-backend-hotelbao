package com.hotelbao.config;

import com.hotelbao.entity.Role;
import com.hotelbao.repository.RoleRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DataInitializer {
    @Bean
    public CommandLineRunner initRoles(RoleRepository roleRepository) {
        return args -> {
            for (Role.RoleType roleType : Role.RoleType.values()) {
                roleRepository.findByName(roleType)
                    .orElseGet(() -> roleRepository.save(new Role(roleType)));
            }
        };
    }
} 