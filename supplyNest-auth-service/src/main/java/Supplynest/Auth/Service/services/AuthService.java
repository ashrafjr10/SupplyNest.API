package Supplynest.Auth.Service.services;

import Supplynest.Auth.Service.constants.AppConstants;
import Supplynest.Auth.Service.dtos.CommonResponse;
import Supplynest.Auth.Service.dtos.RegisterRequest;
import Supplynest.Auth.Service.dtos.RegisterResponse;
import Supplynest.Auth.Service.exceptions.ServiceException;
import Supplynest.Auth.Service.models.Role;
import Supplynest.Auth.Service.models.User;
import Supplynest.Auth.Service.repositories.RoleRepository;
import Supplynest.Auth.Service.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public CommonResponse register(RegisterRequest request) {

        if (userRepository.existsByEmail(request.getEmail())) {
            throw new ServiceException(String.format(AppConstants.MESSAGE_EXISTS, "Email"));
        }

        if (userRepository.existsByMobileNumber(request.getMobileNumber())) {
            throw new ServiceException(String.format(AppConstants.MESSAGE_EXISTS, "Mobile number"));
        }

        Role role = roleRepository.findById(request.getRoleId())
                .orElseThrow(() -> new ServiceException(String.format(AppConstants.NOT_FOUND, "Role")));

        User user = User.builder()
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .email(request.getEmail())
                .mobileNumber(request.getMobileNumber())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(role)
                .enabled(true)
                .accountNonLocked(true)
                .emailVerified(false)
                .mobileVerified(false)
                .build();

        User savedUser = userRepository.save(user);

        RegisterResponse data = RegisterResponse.builder()
                .userId(savedUser.getUserId())
                .firstName(savedUser.getFirstName())
                .lastName(savedUser.getLastName())
                .email(savedUser.getEmail())
                .mobileNumber(savedUser.getMobileNumber())
                .roleId(role.getRoleId())
                .roleName(role.getName())
                .build();

        return CommonResponse.builder()
                .status(AppConstants.STATUS_CREATED)
                .message(AppConstants.MESSAGE_REGISTERED_SUCCESS)
                .data(data)
                .build();
    }

    public CommonResponse login() {
        return CommonResponse.builder()
                .status(AppConstants.STATUS_SUCCESS)
                .message(AppConstants.MESSAGE_LOGIN_SUCCESS)
                .build();
    }
}
