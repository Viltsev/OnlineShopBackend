package com.online_shop.onlineShop.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/hello_world")
public class DemoController {
    @GetMapping
    public String example() {
        return "Hello, world!";
    }
}
