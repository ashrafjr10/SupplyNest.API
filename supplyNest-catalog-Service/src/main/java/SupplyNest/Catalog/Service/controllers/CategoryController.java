package SupplyNest.Catalog.Service.controllers;

import SupplyNest.Catalog.Service.dtos.CreateCategoryRequestDTO;
import SupplyNest.Catalog.Service.services.CategoryService;
import SupplyNest.Common.dtos.CommonResponse;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/businessGroup/{businessGroupCode}/business/{businessCode}/categories")
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryService categoryService;

    @PostMapping("/create")
    public ResponseEntity<CommonResponse> createCategory(@RequestBody CreateCategoryRequestDTO requestDTO,
                                                         @PathVariable("businessGroupCode") String businessGroupCode,
                                                         @PathVariable("businessCode") String businessCode,
                                                         HttpServletRequest httpServletRequest) {
        CommonResponse response = categoryService.createCategory(requestDTO, businessCode, businessGroupCode, httpServletRequest);
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @PutMapping("/{categoryId}/update")
    public ResponseEntity<CommonResponse> updateCategory(@RequestBody CreateCategoryRequestDTO requestDTO,
                                                         @PathVariable("categoryId") UUID categoryId,
                                                         @PathVariable("businessGroupCode") String businessGroupCode,
                                                         @PathVariable("businessCode") String businessCode,
                                                         HttpServletRequest httpServletRequest) {
        CommonResponse response = categoryService.updateCategory(requestDTO, categoryId, businessCode, businessGroupCode, httpServletRequest);
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @DeleteMapping("/{categoryId}")
    ResponseEntity<CommonResponse> deleteCategory(@PathVariable("categoryId") UUID categoryId,
                                                  @PathVariable("businessGroupCode") String businessGroupCode,
                                                  @PathVariable("businessCode") String businessCode,
                                                  HttpServletRequest httpServletRequest) {
        CommonResponse response = categoryService.deleteCategory(categoryId, businessCode, businessGroupCode, httpServletRequest);
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @GetMapping("/{categoryId}")
    public ResponseEntity<CommonResponse> getCategory(@PathVariable("categoryId") UUID categoryId,
                                                      @PathVariable("businessGroupCode") String businessGroupCode,
                                                      @PathVariable("businessCode") String businessCode,
                                                      HttpServletRequest httpServletRequest) {
        CommonResponse response = categoryService.getCategory(categoryId, businessCode, businessGroupCode, httpServletRequest);
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @GetMapping("/all")
    public ResponseEntity<CommonResponse> getAllCategory(@PathVariable("businessGroupCode") String businessGroupCode,
                                                         @PathVariable("businessCode") String businessCode,
                                                         HttpServletRequest httpServletRequest) {
        CommonResponse response = categoryService.getAllCategory(businessCode, businessGroupCode, httpServletRequest);
        return ResponseEntity.status(response.getStatus()).body(response);
    }
}
