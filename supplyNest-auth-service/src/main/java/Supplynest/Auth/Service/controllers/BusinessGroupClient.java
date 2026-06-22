package Supplynest.Auth.Service.controllers;

import Supplynest.Auth.Service.dtos.CommonResponse;
import Supplynest.Auth.Service.dtos.CreateBusinessGroupRequestDTO;
import jakarta.validation.Valid;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@FeignClient(name = "SUPPLYNEST-BUSINESS-SERVICE")
public interface BusinessGroupClient {

    @PostMapping("/supplynest/business-group/create")
    ResponseEntity<?> createBusinessGroup(@RequestBody @Valid CreateBusinessGroupRequestDTO request);
}
