package SupplyNest.Catalog.Service.controllers;

import SupplyNest.Catalog.Service.dtos.CreateBrandRequestDTO;
import SupplyNest.Catalog.Service.models.Brand;
import SupplyNest.Catalog.Service.services.BrandService;
import SupplyNest.Common.dtos.CommonResponse;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/businessGroup/{businessGroupCode}/business/{businessCode}/brands")
@AllArgsConstructor
public class BrandController {

    private final BrandService brandService;

    @PostMapping("/create")
    public ResponseEntity<CommonResponse> createBrand(@RequestBody CreateBrandRequestDTO requestDTO,
                                                      @PathVariable String businessGroupCode,
                                                      @PathVariable String businessCode,
                                                      HttpServletRequest httpServletRequest) {
        CommonResponse response = brandService.createBrand(requestDTO, businessCode, businessGroupCode, httpServletRequest);
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @PutMapping("/{brandId}")
    public ResponseEntity<CommonResponse> updateBrand(@RequestBody CreateBrandRequestDTO requestDTO,
                                                      @PathVariable UUID brandId,
                                                      @PathVariable String businessGroupCode,
                                                      @PathVariable String businessCode,
                                                      HttpServletRequest httpServletRequest) {
        CommonResponse response = brandService.updateBrand(requestDTO, brandId, businessCode, businessGroupCode, httpServletRequest);
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @GetMapping("/{brandId}")
    public ResponseEntity<CommonResponse> getBrand(@PathVariable UUID brandId,
                                                   @PathVariable String businessGroupCode,
                                                   @PathVariable String businessCode,
                                                   HttpServletRequest httpServletRequest) {
        CommonResponse response = brandService.getBrand(brandId, businessCode, businessGroupCode, httpServletRequest);
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @GetMapping("/all")
    private ResponseEntity<CommonResponse> getAllBrand(@PathVariable String businessGroupCode,
                                                       @PathVariable String businessCode,
                                                       HttpServletRequest httpServletRequest){
        CommonResponse response = brandService.getAllBrand(businessCode, businessGroupCode, httpServletRequest);
        return ResponseEntity.status(response.getStatus()).body(response);
    }
}
