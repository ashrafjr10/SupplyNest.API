package Supplynest.Auth.Service.controllers;

import SupplyNest.Common.config.FeignConfig;
import SupplyNest.Common.dtos.CommonResponse;
import Supplynest.Auth.Service.dtos.CreateBusinessGroupRequestDTO;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@FeignClient(name = "SUPPLYNEST-BUSINESS-SERVICE", configuration = FeignConfig.class)
public interface BusinessGroupClient {

    @PostMapping("/business-group/create")
    ResponseEntity<?> createBusinessGroup(@RequestBody @Valid CreateBusinessGroupRequestDTO request);

    @GetMapping("/business-group/{businessGroupId}")
    public ResponseEntity<CommonResponse> getBusinessGroupById(@PathVariable("businessGroupId") UUID businessGroupId);
}
