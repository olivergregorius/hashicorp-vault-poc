package de.effitient.dev.hashicorpvaultpoc.rest;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloWorldController {

    @GetMapping(path = "/api/v1/helloworld")
    public ResponseEntity<String> helloworld() {
        return ResponseEntity.ok("Hello World");
    }
}
