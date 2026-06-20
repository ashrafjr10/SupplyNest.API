package SupplyNest.Business.Service.controllers;

import SupplyNest.Business.Service.dtos.CommonResponse;
import SupplyNest.Business.Service.dtos.CreateBusinessGroupRequestDTO;
import SupplyNest.Business.Service.models.BusinessGroup;
import SupplyNest.Business.Service.services.BusinessGroupService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/supplynest/business-group")
@RequiredArgsConstructor
public class BusinessGroupController {

    private final BusinessGroupService businessGroupService;

    @PostMapping("/create")
    public ResponseEntity<?> createBusinessGroup(@RequestBody @Valid CreateBusinessGroupRequestDTO request) {
        CommonResponse response = businessGroupService.createBusinessGroup(request);
        return ResponseEntity.status(response.getStatus()).body(response);
    }
}
