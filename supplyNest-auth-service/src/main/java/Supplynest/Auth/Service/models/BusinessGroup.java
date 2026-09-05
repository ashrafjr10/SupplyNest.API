package Supplynest.Auth.Service.models;

import SupplyNest.Common.constants.RegexPatterns;
import Supplynest.Auth.Service.enums.modelEnums;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BusinessGroup extends BaseEntity{

    private UUID businessGroupId;
    private String firstName;
    private String lastName;
    private String businessGroupCode;
    private String status;
    private String phoneNumber;
    private String email;
    private UUID userId;

    private List<Business> businessList = new ArrayList<>();

}

