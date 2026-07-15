package com.nestly.notification.client;

import com.nestly.notification.dto.UserDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "auth-service", url = "${clients.auth-service-url}")
public interface UserClient {

    @GetMapping("/users/{id}")
    UserDto getUser(@PathVariable("id") String id);
}
