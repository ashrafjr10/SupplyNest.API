package SupplyNest.Business.Service.controllers;

import SupplyNest.Business.Service.dtos.CreateBusinessGroupRequestDTO;
import SupplyNest.Business.Service.dtos.CreateBusinessRequestDTO;
import SupplyNest.Business.Service.services.BusinessGroupService;
import SupplyNest.Common.dtos.CommonResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    @PostMapping("/{businessGroupCode}/business/create")
    public ResponseEntity<?> createBusiness(@RequestBody @Valid CreateBusinessRequestDTO requestDTO,
                                            @PathVariable("businessGroupCode") String businessGroupCode,
                                            HttpServletRequest servletRequest) {
        CommonResponse response = businessGroupService.createBusiness(requestDTO, businessGroupCode, servletRequest);
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @GetMapping("/all")
    public ResponseEntity<?> getAllBusinessGroups(HttpServletRequest request,
                                                  @RequestParam("page") int page,
                                                  @RequestParam("size") int size) {
        CommonResponse response = businessGroupService.getAllBusinessGroups(request, page, size);
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @GetMapping("/test")
    public ResponseEntity<?> test(HttpServletRequest request) {

        String userId = request.getHeader("X-User-Id");
        String role = request.getHeader("X-Role");

        return ResponseEntity.ok(request);
    }
}
