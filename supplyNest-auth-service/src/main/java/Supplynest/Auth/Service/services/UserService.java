package Supplynest.Auth.Service.services;

import SupplyNest.Common.constants.AppConstants;
import SupplyNest.Common.dtos.CommonResponse;
import Supplynest.Auth.Service.models.User;
import Supplynest.Auth.Service.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public CommonResponse getUser(UUID userId) {
        Optional<User> userOptional = userRepository.findById(userId);
        if (userOptional.isEmpty()) {
            return CommonResponse.builder().status(AppConstants.STATUS_NOT_FOUND).message(String.format(AppConstants.NOT_FOUND, "User")).build();
        }
        User user = userOptional.get();

        return CommonResponse.builder().status(AppConstants.STATUS_SUCCESS).message(AppConstants.MESSAGE_SUCCESS).data(user).build();
    }
}
