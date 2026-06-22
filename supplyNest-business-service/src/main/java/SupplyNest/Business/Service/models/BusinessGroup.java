package SupplyNest.Business.Service.models;

import SupplyNest.Business.Service.constants.RegexPatterns;
import SupplyNest.Business.Service.enums.modelEnums;
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
@Entity
@Builder
@Table(name = "business_group")
public class BusinessGroup extends BaseEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID businessGroupId;

    @NotBlank(message = "first name is required")
    @Pattern(regexp = RegexPatterns.REGEX_LETTERS_AND_SPACES, message = "first name should contain only alphabets and spaces")
    private String firstName;

    @Pattern(regexp = RegexPatterns.REGEX_LETTERS_AND_SPACES, message = "Last name should contain only alphabets and spaces")
    private String lastName;

    @NotBlank(message = "Business Group Code is required")
    private String businessGroupCode;

    @NotNull(message = "Status is required")
    @Enumerated(EnumType.STRING)
    private modelEnums.BusinessGroupStatus status;

    @NotBlank(message = "Phone Number is required")
    @Size(max = 10, min = 10, message = "Phone number should be 10 digits")
    @Pattern(regexp = RegexPatterns.REGEX_PHONE, message = "Phone number only contain numbers")
    private String phoneNumber;

    @NotBlank(message = "Email is required")
    @Pattern(regexp = RegexPatterns.REGEX_EMAIL, message = "Email should be in valid format")
    @Size(max = 100, min = 3, message = "Email should be between 3 and 100 characters")
    private String email;

    @NotNull(message = "user is required")
    private UUID userId;

    @Builder.Default
    @OneToMany(mappedBy = "businessGroup", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Business> businessList = new ArrayList<>();

    public BusinessGroup addBusiness(Business business){
        businessList.add(business);
        business.setBusinessGroup(this);
        return this;
    }

    public BusinessGroup removeBusiness(Business business){
        businessList.remove(business);
        business.setBusinessGroup(null);
        return this;
    }
}
