package SupplyNest.Catalog.Service.controllers;

import SupplyNest.Common.dtos.CommonResponse;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "SUPPLYNEST-BUSINESS-SERVICE")
public interface BusinessGroupClient {

    @GetMapping("/business-group/{businessGroupCode}")
    ResponseEntity<CommonResponse> getBusinessGroups(@PathVariable("businessGroupCode") String businessGroupCode,
                                                     HttpServletRequest request);

    @GetMapping("/business-group/{businessGroupCode}/business/{businessCode}")
    ResponseEntity<CommonResponse> getBusiness(@PathVariable("businessGroupCode") String businessGroupCode,
                                               @PathVariable("businessCode") String businessCode,
                                               HttpServletRequest httpServletRequest);
}
