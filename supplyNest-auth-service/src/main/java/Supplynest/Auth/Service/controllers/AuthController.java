package Supplynest.Auth.Service.controllers;

import Supplynest.Auth.Service.dtos.*;
import Supplynest.Auth.Service.services.AuthService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/supplyNest/auth/")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequest registerRequest) {
        CommonResponse response = authService.register(registerRequest);
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest, HttpServletRequest httpServletRequest) {
        CommonResponse response = authService.login(loginRequest, httpServletRequest);
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @PostMapping("/create-user")
    public ResponseEntity<?> login(@RequestBody CreateUserDTO createUser, HttpServletRequest httpServletRequest) {
        CommonResponse response = authService.createUser(createUser, httpServletRequest);
        return ResponseEntity.status(response.getStatus()).body(response);
    }
}
