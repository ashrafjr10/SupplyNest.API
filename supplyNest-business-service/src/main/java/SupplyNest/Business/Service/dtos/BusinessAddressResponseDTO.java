package SupplyNest.Business.Service.dtos;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Setter
@Getter
@Builder
public class BusinessAddressResponseDTO {
    private UUID businessAddressId;
    private String addressLine1;
    private String addressLine2;
    private String city;
    private String state;
    private String pinCode;
}
