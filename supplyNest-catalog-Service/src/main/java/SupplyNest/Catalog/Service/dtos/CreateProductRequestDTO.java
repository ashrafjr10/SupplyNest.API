package SupplyNest.Catalog.Service.dtos;

import SupplyNest.Catalog.Service.models.Brand;
import SupplyNest.Catalog.Service.models.Category;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Setter
@Getter
public class CreateProductRequestDTO {

    @NotBlank(message = "Product name is required")
    @Size(max = 100, min = 3, message = "Product name must be between 3 and 100 characters")
    private String productName;

    @NotBlank(message = "Description is required")
    @Size(max = 500, min = 3, message = "Description must be between 3 and 500 characters")
    private String description;

    @NotNull(message = "Display image is required")
    private MultipartFile displayImage;

    @Size(max = 5, message = "Maximum 5 images are allowed")
    private List<MultipartFile> images;

    private UUID categoryId;

    private UUID brandId;
}
