package com.store.oncommerce_web.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.ResponseEntity;

@RestController
public class NotificationController {

    @PostMapping("/notify")
    public ResponseEntity<String> notifyUser(@RequestParam String message) {
        // Puedes realizar otras operaciones aquí si es necesario.
        return ResponseEntity.ok(message);
    }
}

