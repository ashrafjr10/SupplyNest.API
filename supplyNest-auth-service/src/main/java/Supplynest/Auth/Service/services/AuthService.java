package Supplynest.Auth.Service.services;

import Supplynest.Auth.Service.constants.AppConstants;
import Supplynest.Auth.Service.dtos.CommonResponse;
import Supplynest.Auth.Service.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;

    public static CommonResponse register() {
        return CommonResponse.builder().status(AppConstants.STATUS_SUCCESS).message("Registration Successful").build();
    }

    public static CommonResponse login() {
        return CommonResponse.builder().status(AppConstants.STATUS_SUCCESS).message("Login Successful").build();
    }
}
