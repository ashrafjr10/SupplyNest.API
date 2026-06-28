package SupplyNest.Business.Service.controllers;

import SupplyNest.Business.Service.config.FeignConfig;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@FeignClient(name = "SUPPLYNEST-FILE-SERVICE", configuration = FeignConfig.class)
public interface FileClient {

    @PostMapping(value = "/files/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<String> uploadFile(
            @RequestPart("file") MultipartFile file,
            @RequestPart("path") String path);

    @DeleteMapping("/files")
    public ResponseEntity<String> deleteFile(
            @RequestParam String path);

    @GetMapping("/files/download")
    public ResponseEntity<Resource> downloadFile(
            @RequestParam String path);
}
