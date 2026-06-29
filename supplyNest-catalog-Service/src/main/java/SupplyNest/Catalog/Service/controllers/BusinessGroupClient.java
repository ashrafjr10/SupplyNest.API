package SupplyNest.Catalog.Service.controllers;

import SupplyNest.Common.config.FeignConfig;
import SupplyNest.Common.dtos.CommonResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "SUPPLYNEST-BUSINESS-SERVICE", configuration = FeignConfig.class)
public interface BusinessGroupClient {

    @GetMapping("/business-group/{businessGroupCode}")
    ResponseEntity<CommonResponse> getBusinessGroups(@PathVariable("businessGroupCode") String businessGroupCode);

    @GetMapping("/business-group/{businessGroupCode}/business/{businessCode}")
    public ResponseEntity<CommonResponse> getBusiness(@PathVariable("businessGroupCode") String businessGroupCode,
                                                      @PathVariable("businessCode") String businessCode);
}
