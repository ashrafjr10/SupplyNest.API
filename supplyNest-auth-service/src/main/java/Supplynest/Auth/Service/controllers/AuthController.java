package Supplynest.Auth.Service.controllers;

import Supplynest.Auth.Service.dtos.CommonResponse;
import Supplynest.Auth.Service.dtos.LoginRequest;
import Supplynest.Auth.Service.dtos.RegisterRequest;
import Supplynest.Auth.Service.dtos.UserDO;
import Supplynest.Auth.Service.services.AuthService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/supplyNest/auth/")
public class AuthController {

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequest registerRequest) {
        CommonResponse response = AuthService.register(registerRequest);
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest, HttpServletRequest httpServletRequest) {
        CommonResponse response = AuthService.login(loginRequest, httpServletRequest);
        return ResponseEntity.status(response.getStatus()).body(response);
    }
}
