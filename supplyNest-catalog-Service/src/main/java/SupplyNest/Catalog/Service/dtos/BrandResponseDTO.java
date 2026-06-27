package SupplyNest.Catalog.Service.dtos;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Setter
@Getter
@Builder
public class BrandResponseDTO {
    private UUID brandId;
    private String brandName;
}
