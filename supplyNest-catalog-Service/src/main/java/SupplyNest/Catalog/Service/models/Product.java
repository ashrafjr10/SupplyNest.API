package SupplyNest.Catalog.Service.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
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
@Table(name = "product")
@Builder
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID productId;

    @NotBlank(message = "Product name is required")
    @Size(max = 100, min = 3, message = "Product name must be between 3 and 100 characters")
    private String productName;

    @NotBlank(message = "Description is required")
    @Size(max = 500, min = 3, message = "Description must be between 3 and 500 characters")
    private String description;

    private String displayImage;

    @Size(max = 5, message = "Maximum 5 images are allowed")
    private List<String> images = new ArrayList<>();

    @NotNull(message = "Business ID is required")
    private UUID businessId;

    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;

    @ManyToOne
    @JoinColumn(name = "brand_id")
    private Brand brand;
}
