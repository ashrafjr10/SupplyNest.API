package SupplyNest.Catalog.Service.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.util.List;
import java.util.UUID;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "brand")
@Builder
public class Brand {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID brandId;

    @NotBlank(message = "Brand name is required")
    @Size(max = 100, min = 3, message = "Brand name must be between 3 and 100 characters")
    private String brandName;

    @NotNull(message = "Business ID is required")
    private UUID businessId;

    @OneToMany(mappedBy = "brand", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Product> products;
}
