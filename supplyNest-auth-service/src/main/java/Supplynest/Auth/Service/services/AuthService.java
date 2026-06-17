package Supplynest.Auth.Service.services;

import Supplynest.Auth.Service.constants.AppConstants;
import Supplynest.Auth.Service.dtos.CommonResponse;
import Supplynest.Auth.Service.dtos.LoginRequest;
import Supplynest.Auth.Service.dtos.RegisterRequest;
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
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthService {

    private static UserRepository userRepository;
    private static RoleRepository roleRepository;
    private static PasswordEncoder passwordEncoder;
    private static JwtUtils jwtUtils;
    private static RoleFormatterForUI roleFormatterForUI;
    private static UserLogsRepository userLogsRepository;

    public static CommonResponse login(LoginRequest loginRequest, HttpServletRequest httpServletRequest) {

        String ipAddress = RequestUtils.getClientIp(httpServletRequest);
        String userAgent = RequestUtils.getUserAgent(httpServletRequest);
        String device = RequestUtils.getDevice(userAgent);
        String browser = RequestUtils.getBrowser(userAgent);

        Optional<User> userOptional = userRepository.findByMobileNumberOrEmail(loginRequest.getPhoneOrEmail());
        if (userOptional.isEmpty()) {
            return CommonResponse.builder().message(AppConstants.MESSAGE_INVALID_CREDENTIALS).status(AppConstants.STATUS_NOT_FOUND).build();
        }
        User user = userOptional.get();

        if (!passwordEncoder.matches(loginRequest.getPassword(), user.getPassword())) {
            logLogin(user, modelEnums.LoginStatus.FAILED, AppConstants.MESSAGE_PASSWORD_DO_NOT_MATCH, ipAddress, device, browser);
            return CommonResponse.builder().message(AppConstants.MESSAGE_INVALID_CREDENTIALS).status(AppConstants.STATUS_UNAUTHORIZED).build();
        }

        String accessToken = jwtUtils.generateAccessToken(loginRequest.getPhoneOrEmail(), user.getUserId(), null, null, null);
        String refreshToken = jwtUtils.generateRefreshToken(loginRequest.getPhoneOrEmail(), null);

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

    public static CommonResponse register(RegisterRequest registerRequest) {
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

        userRepository.save(user);

        return CommonResponse.builder().status(AppConstants.STATUS_SUCCESS).message("User Registered Successfully").build();
    }

    private static void logLogin(User user, modelEnums.LoginStatus status,
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
}
