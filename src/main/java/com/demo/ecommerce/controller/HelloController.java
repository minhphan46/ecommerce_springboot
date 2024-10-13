package com.demo.ecommerce.controller;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {

    @GetMapping("/")
    public String hello(HttpServletRequest request) {
        String hostname = System.getenv("HOSTNAME");

        String ipAddress = request.getHeader("X-Forwarded-For");
        if (ipAddress == null || ipAddress.isEmpty()) {
            ipAddress = request.getRemoteAddr();  // Lấy IP thực từ request
        }

        return "Hello from instance: " + hostname + ". Client IP: " + ipAddress;
    }

    @GetMapping("/delay")
    public String delayResponse(@RequestParam("time") int time, HttpServletRequest request) {
        try {
            Thread.sleep(time * 1000L);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            return "Error occurred while waiting.";
        }

        String hostname = System.getenv("HOSTNAME");

        String ipAddress = request.getHeader("X-Forwarded-For");
        if (ipAddress == null || ipAddress.isEmpty()) {
            ipAddress = request.getRemoteAddr();  // Lấy IP thực từ request
        }

        return "Hello after delay " + time + " seconds from instance: " + hostname + ". Client IP: " + ipAddress;
    }

}
