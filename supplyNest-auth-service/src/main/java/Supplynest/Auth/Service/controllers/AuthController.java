package Supplynest.Auth.Service.controllers;

import Supplynest.Auth.Service.dtos.CommonResponse;
import Supplynest.Auth.Service.services.AuthService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/supplyNest/auth/")
public class AuthController {

    @PostMapping("/register")
    public ResponseEntity<?> register() {
        CommonResponse response = AuthService.register();
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @PostMapping("/login")
    public ResponseEntity<?> login() {
        CommonResponse response = AuthService.login();
        return ResponseEntity.status(response.getStatus()).body(response);
    }
}
