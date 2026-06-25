package Supplynest.Auth.Service.dtos;

import lombok.*;
import org.aspectj.apache.bcel.classfile.Code;

import java.util.UUID;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserDO {
    private UUID userId;
    private String phoneOrEmail;
    private String userType;
//    private RoleResponseDTO role;
    private String businessCode;
    private String businessGroupCode;
}
