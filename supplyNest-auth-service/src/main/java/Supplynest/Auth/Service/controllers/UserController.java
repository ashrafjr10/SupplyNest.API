package Supplynest.Auth.Service.controllers;

import SupplyNest.Common.dtos.CommonResponse;
import Supplynest.Auth.Service.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/{userId}")
    public ResponseEntity<CommonResponse> getUser(@RequestParam UUID userId) {
        CommonResponse response = userService.getUser(userId);
        return ResponseEntity.status(response.getStatus()).body(response);
    }
}
