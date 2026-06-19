package Supplynest.Auth.Service.dtos;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@Builder
public class RegisterResponse {
    private UUID userId;
    private String firstName;
    private String lastName;
    private String email;
    private String mobileNumber;
    private UUID roleId;
    private String roleName;
}
