package Supplynest.Auth.Service.controllers;

import Supplynest.Auth.Service.dtos.CommonResponse;
import Supplynest.Auth.Service.dtos.RegisterRequest;
import Supplynest.Auth.Service.services.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/supplyNest/auth/")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<?> register(@Valid @RequestBody RegisterRequest request) {
        CommonResponse response = authService.register(request);
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @PostMapping("/login")
    public ResponseEntity<?> login() {
        CommonResponse response = authService.login();
        return ResponseEntity.status(response.getStatus()).body(response);
    }
}
