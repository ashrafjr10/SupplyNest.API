package Supplynest.Auth.Service.services;

import SupplyNest.Common.dtos.CommonResponse;
import SupplyNest.Common.dtos.TokenValidationResponse;
import Supplynest.Auth.Service.constants.AppConstants;
import Supplynest.Auth.Service.controllers.BusinessGroupClient;
import Supplynest.Auth.Service.dtos.*;
import Supplynest.Auth.Service.enums.modelEnums;
import Supplynest.Auth.Service.models.Role;
import Supplynest.Auth.Service.models.User;
import Supplynest.Auth.Service.models.UserLogs;
import Supplynest.Auth.Service.repositories.RoleRepository;
import Supplynest.Auth.Service.repositories.UserLogsRepository;
import Supplynest.Auth.Service.repositories.UserRepository;
import Supplynest.Auth.Service.utils.JwtUtils;
import Supplynest.Auth.Service.utils.RequestUtils;
import Supplynest.Auth.Service.utils.RoleFormatterForUI;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final JwtUtils jwtUtils;
    private final PasswordEncoder passwordEncoder;
    private final RoleFormatterForUI roleFormatterForUI;
    private final UserLogsRepository userLogsRepository;
    private final BusinessGroupClient businessGroupClient;

    public CommonResponse login(LoginRequest loginRequest, HttpServletRequest httpServletRequest) {

        String ipAddress = RequestUtils.getClientIp(httpServletRequest);
        String userAgent = RequestUtils.getUserAgent(httpServletRequest);
        String device = RequestUtils.getDevice(userAgent);
        String browser = RequestUtils.getBrowser(userAgent);

        Optional<User> userOptional = userRepository.findByMobileNumberOrEmail(loginRequest.getPhoneOrEmail());
        if (userOptional.isEmpty()) {
            System.out.println("Login failed: User not found for phoneOrEmail: " + loginRequest.getPhoneOrEmail());
            return CommonResponse.builder().message(AppConstants.MESSAGE_INVALID_CREDENTIALS).status(AppConstants.STATUS_NOT_FOUND).build();
        }
        User user = userOptional.get();

        if (!passwordEncoder.matches(loginRequest.getPassword(), user.getPassword())) {
            System.out.println("Login failed: Password mismatch for user: " + user.getMobileNumber() + " / " + user.getEmail());
            logLogin(user, modelEnums.LoginStatus.FAILED, AppConstants.MESSAGE_PASSWORD_DO_NOT_MATCH, ipAddress, device, browser);
            return CommonResponse.builder().message(AppConstants.MESSAGE_INVALID_CREDENTIALS).status(AppConstants.STATUS_UNAUTHORIZED).build();
        }

        String accessToken = jwtUtils.generateAccessToken(loginRequest.getPhoneOrEmail(), user.getUserId(), null, null, null, user.getRole());
        String refreshToken = jwtUtils.generateRefreshToken(loginRequest.getPhoneOrEmail(), null, user.getRole());

        user.setRefreshToken(refreshToken);
        userRepository.save(user);

        logLogin(user, modelEnums.LoginStatus.SUCCESS, null, ipAddress, device, browser);

        Map<String, Object> data = new HashMap<>();
        data.put("role", roleFormatterForUI.formatRole(user.getRole()));
        data.put("userType", null);
        data.put("businessGroupCode", null);
        data.put("businessCode", null);
        data.put("phoneOrEmail", loginRequest.getPhoneOrEmail());
        data.put("staffCode", null);
        data.put("subscribed", null);
        data.put("accessToken", accessToken);
        data.put("refreshToken", refreshToken);

        return CommonResponse.builder().status(AppConstants.STATUS_SUCCESS).message("Login Successful").data(data).build();
    }

    @Transactional
    public CommonResponse register(RegisterRequest registerRequest) {

        if (!registerRequest.getEmail().isEmpty() && userRepository.existsByEmail(registerRequest.getEmail()))
            return CommonResponse.builder().message(String.format(AppConstants.MESSAGE_EXISTS, "Email")).status(AppConstants.STATUS_CONFLICT).build();

        if (!registerRequest.getMobileNumber().isEmpty() && userRepository.existsByMobileNumber(registerRequest.getMobileNumber()))
            return CommonResponse.builder().message(String.format(AppConstants.MESSAGE_EXISTS, "Mobile Number")).status(AppConstants.STATUS_CONFLICT).build();

        Optional<Role> roleOptional = roleRepository.findById(registerRequest.getRoleId());
        if (roleOptional.isEmpty()) {
            return CommonResponse.builder().message(String.format(AppConstants.NOT_FOUND, "Role")).status(AppConstants.STATUS_NOT_FOUND).build();
        }
        Role role = roleOptional.get();

        User user = User.builder()
                .email(registerRequest.getEmail())
                .mobileNumber(registerRequest.getMobileNumber())
                .firstName(registerRequest.getFirstName())
                .lastName(registerRequest.getLastName())
                .password(passwordEncoder.encode(registerRequest.getPassword()))
                .role(role)
                .build();
        user.prePersist();

        user = userRepository.save(user);

        registerRequest.getCreateBusinessGroupRequestDTO().setUserId(user.getUserId());

        ResponseEntity<?> response = businessGroupClient.createBusinessGroup(registerRequest.getCreateBusinessGroupRequestDTO());

        return CommonResponse.builder().status(AppConstants.STATUS_SUCCESS).message("User Registered Successfully").build();
    }

    private void logLogin(User user, modelEnums.LoginStatus status,
                                 String reason, String ip, String device, String browser) {

        UserLogs log = UserLogs.builder()
                .groupUser(user)
                .status(status)
                .failureReason(reason)
                .loginIp(ip)
                .loginDevice(device)
                .loginBrowser(browser)
                .build();

        userLogsRepository.save(log);
    }

    public CommonResponse createUser(CreateUserDTO registerRequest, HttpServletRequest httpServletRequest) {
        if (!registerRequest.getEmail().isEmpty() && userRepository.existsByEmail(registerRequest.getEmail()))
            return CommonResponse.builder().message(String.format(AppConstants.MESSAGE_EXISTS, "Email")).status(AppConstants.STATUS_CONFLICT).build();

        if (!registerRequest.getMobileNumber().isEmpty() && userRepository.existsByMobileNumber(registerRequest.getMobileNumber()))
            return CommonResponse.builder().message(String.format(AppConstants.MESSAGE_EXISTS, "Mobile Number")).status(AppConstants.STATUS_CONFLICT).build();

        Optional<Role> roleOptional = roleRepository.findById(registerRequest.getRoleId());
        if (roleOptional.isEmpty()) {
            return CommonResponse.builder().message(String.format(AppConstants.NOT_FOUND, "Role")).status(AppConstants.STATUS_NOT_FOUND).build();
        }
        Role role = roleOptional.get();

        User user = User.builder()
                .email(registerRequest.getEmail())
                .mobileNumber(registerRequest.getMobileNumber())
                .firstName(registerRequest.getName())
                .lastName(registerRequest.getName())
                .password(passwordEncoder.encode(registerRequest.getPassword()))
                .role(role)
                .build();
        user.prePersist();

        userRepository.save(user);

        return CommonResponse.builder().status(AppConstants.STATUS_SUCCESS).build();
    }

    public TokenValidationResponse validateToken(String token) {
        return TokenValidationResponse.builder().isValid(jwtUtils.validateToken(token)).user(jwtUtils.getUserDataFromToken(token)).build();
    }
}
