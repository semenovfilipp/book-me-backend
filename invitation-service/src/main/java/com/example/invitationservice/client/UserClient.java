package com.example.invitationservice.client;

import com.example.invitationservice.model.Invitation;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@FeignClient(name= "user-service", url = "${application.config.user-url}")
public interface UserClient {

    @GetMapping("/currentUser")
    String getCurrentUser();
}