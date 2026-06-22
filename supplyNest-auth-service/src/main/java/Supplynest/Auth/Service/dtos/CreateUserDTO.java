package Supplynest.Auth.Service.dtos;

import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Setter
@Getter
public class CreateUserDTO {
    private String name;
    private String email;
    private String mobileNumber;
    private String password;
    private UUID roleId;
}
