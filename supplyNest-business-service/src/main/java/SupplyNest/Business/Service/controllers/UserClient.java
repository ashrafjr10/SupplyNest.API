package SupplyNest.Business.Service.controllers;

import SupplyNest.Common.dtos.CommonResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.UUID;

@FeignClient(name = "SUPPLYNEST-AUTH-SERVICE")
public interface UserClient {

    @GetMapping("/user/{userId}")
    public ResponseEntity<CommonResponse> getUser(@RequestParam UUID userId);

}
