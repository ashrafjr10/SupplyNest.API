package SupplyNest.Business.Service.models;

import SupplyNest.Business.Service.constants.RegexPatterns;
import SupplyNest.Business.Service.enums.modelEnums;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.util.List;
import java.util.UUID;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Builder
@Table(name = "business")
public class Business extends BaseEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID businessId;

    @NotBlank(message = "Business Name is required")
    @Pattern(regexp = RegexPatterns.REGEX_LETTERS_AND_SPACES, message = "Business Name should contain only alphabets and spaces")
    @Size(max = 60, min = 3, message = "Business Name should be between 3 and 60 characters")
    private String businessName;

    @NotBlank(message = "Business Code is required")
    private String businessCode;

    @NotNull(message = "Type is required")
    @Enumerated(EnumType.STRING)
    private modelEnums.BusinessType type;

    @Pattern(regexp = RegexPatterns.REGEX_GSTIN, message = "GST number should be in valid format")
    private String gstNumber;

    @NotNull(message = "Status is required")
    @Enumerated(EnumType.STRING)
    private modelEnums.BusinessStatus status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "business_group_id", nullable = false)
    private BusinessGroup businessGroup;

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "business_address_id")
    private BusinessAddress businessAddress;

    @OneToMany(mappedBy = "business", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<SubscribeBusiness> subscribeList;
}
