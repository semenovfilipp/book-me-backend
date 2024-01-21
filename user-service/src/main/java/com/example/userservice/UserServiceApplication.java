package com.example.userservice;

import com.example.userservice.model.auth.RegisterRequest;
import com.example.userservice.model.enums.Role;
import com.example.userservice.service.AuthenticationService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;

@EnableFeignClients
@SpringBootApplication
public class UserServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(UserServiceApplication.class, args);
    }

    @Bean
    public CommandLineRunner commandLineRunner(
            AuthenticationService authenticationService
    ){
        return args -> {
            var admin = RegisterRequest.builder()
                    .firstname("admin")
                    .lastname("admin")
                    .email("admin@mail.com")
                    .password("1234")
                    .role(Role.ADMIN)
                    .build();
            System.out.println("Admin token: " + authenticationService.register(admin).getAccessToken());

            var user = RegisterRequest.builder()
                    .firstname("user")
                    .lastname("user")
                    .email("user@mail.com")
                    .password("1234")
                    .role(Role.USER)
                    .build();
            System.out.println("User token: " + authenticationService.register(user).getAccessToken());
        };
    }

}
