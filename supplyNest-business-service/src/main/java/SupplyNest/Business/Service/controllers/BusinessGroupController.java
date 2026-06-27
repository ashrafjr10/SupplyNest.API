package SupplyNest.Business.Service.controllers;

import SupplyNest.Business.Service.dtos.CreateBusinessGroupRequestDTO;
import SupplyNest.Business.Service.dtos.CreateBusinessRequestDTO;
import SupplyNest.Business.Service.dtos.UpdateBusinessGroupRequestDTO;
import SupplyNest.Business.Service.services.BusinessGroupService;
import SupplyNest.Common.dtos.CommonResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/business-group")
@RequiredArgsConstructor
public class BusinessGroupController {

    private final BusinessGroupService businessGroupService;

    @PostMapping("/create")
    public ResponseEntity<?> createBusinessGroup(@RequestBody @Valid CreateBusinessGroupRequestDTO request) {
        CommonResponse response = businessGroupService.createBusinessGroup(request);
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @PostMapping("/{businessGroupCode}/update")
    public ResponseEntity<?> updateBusinessGroup(@RequestBody @Valid UpdateBusinessGroupRequestDTO requestDTO,
                                                 @PathVariable("businessGroupCode") String businessGroupCode,
                                                 HttpServletRequest servletRequest) {
        CommonResponse response = businessGroupService.updateBusinessGroup(requestDTO, businessGroupCode, servletRequest);
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @GetMapping("/{businessGroupCode}")
    public ResponseEntity<?> getBusinessGroups(@PathVariable("businessGroupCode") String businessGroupCode, HttpServletRequest request) {
        CommonResponse response = businessGroupService.getBusinessGroups(businessGroupCode, request);
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @GetMapping("/all")
    public ResponseEntity<?> getAllBusinessGroups(HttpServletRequest request,
                                                  @RequestParam("page") int page,
                                                  @RequestParam("size") int size) {
        CommonResponse response = businessGroupService.getAllBusinessGroups(request, page, size);
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @PostMapping("/{businessGroupCode}/business/create")
    public ResponseEntity<?> createBusiness(@RequestBody @Valid CreateBusinessRequestDTO requestDTO,
                                            @PathVariable("businessGroupCode") String businessGroupCode,
                                            HttpServletRequest servletRequest) {
        CommonResponse response = businessGroupService.createBusiness(requestDTO, businessGroupCode, servletRequest);
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @PutMapping("/{businessGroupCode}/business/{businessCode}/update")
    public ResponseEntity<?> updateBusiness(@RequestBody @Valid CreateBusinessRequestDTO requestDTO,
                                            @PathVariable("businessGroupCode") String businessGroupCode,
                                            @PathVariable("businessCode") String businessCode,
                                            HttpServletRequest httpServletRequest){
        CommonResponse response = businessGroupService.updateBusiness(requestDTO, businessGroupCode, businessCode, httpServletRequest);
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @PutMapping("/{businessGroupCode}/business/{businessCode}/update-logo")
    public ResponseEntity<?> updateBusiness(@RequestParam MultipartFile logo,
                                            @PathVariable("businessGroupCode") String businessGroupCode,
                                            @PathVariable("businessCode") String businessCode,
                                            HttpServletRequest httpServletRequest){
        CommonResponse response = businessGroupService.updateBusinessLogo(logo, businessGroupCode, businessCode, httpServletRequest);
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @GetMapping("/{businessGroupCode}/business/{businessCode}")
    public ResponseEntity<?> getBusiness(@PathVariable("businessGroupCode") String businessGroupCode,
                                         @PathVariable("businessCode") String businessCode,
                                         HttpServletRequest httpServletRequest){
        CommonResponse response = businessGroupService.getBusiness(businessGroupCode, businessCode, httpServletRequest);
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @GetMapping("/{businessGroupCode}/business-name")
    public ResponseEntity<?> getBusinessNames(@PathVariable("businessGroupCode") String businessGroupCode,
                                              HttpServletRequest httpServletRequest){
        CommonResponse response = businessGroupService.getBusinessNames(businessGroupCode, httpServletRequest);
        return ResponseEntity.status(response.getStatus()).body(response);
    }
}
