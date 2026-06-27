package SupplyNest.Catalog.Service.controllers;

import SupplyNest.Catalog.Service.dtos.CreateProductRequestDTO;
import SupplyNest.Catalog.Service.models.Product;
import SupplyNest.Catalog.Service.services.ProductService;
import SupplyNest.Common.dtos.CommonResponse;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/business-group/{businessGroupCode}/business/{businessCode}/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    @PostMapping("/create")
    public ResponseEntity<?> createProducts(@RequestBody CreateProductRequestDTO product,
                                            @PathVariable String businessGroupCode,
                                            @PathVariable String businessCode,
                                            HttpServletRequest httpServletRequest) {
        CommonResponse response = productService.createProduct(product, businessGroupCode, businessCode, httpServletRequest);
        return ResponseEntity.status(response.getStatus()).body(response);
    }
}
