package com.ucsal.security.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;

@FeignClient(name = "user-service")
public interface UserClient {

    @GetMapping("/internal/users/auth-info")
    Map<String, Object> obterInfoAuth(@RequestParam("email") String email);

    @GetMapping("/internal/users/{id}")
    Map<String, Object> obterUsuarioPorId(@PathVariable("id") Integer id);
}
