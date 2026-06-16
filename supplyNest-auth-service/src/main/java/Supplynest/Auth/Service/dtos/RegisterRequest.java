package Supplynest.Auth.Service.dtos;

import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Setter
@Getter
public class RegisterRequest {
    private String firstName;
    private String lastName;
    private String email;
    private String mobileNumber;
    private String password;
    private UUID roleId;
}
