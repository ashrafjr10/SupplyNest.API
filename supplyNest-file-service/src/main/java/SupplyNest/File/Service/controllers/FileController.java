package SupplyNest.File.Service.controllers;

import SupplyNest.File.Service.dtos.UploadResponse;
import SupplyNest.File.Service.service.FileService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.core.io.Resource;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/files")
@RequiredArgsConstructor
public class FileController {

    private final FileService fileService;

    @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<String> uploadFile(
            @RequestParam("file") MultipartFile file,
            @RequestParam("path") String path) {

        String uploadedPath = fileService.saveFile(file, path);

        return ResponseEntity.ok(uploadedPath);
    }

    @DeleteMapping
    public ResponseEntity<String> deleteFile(
            @RequestParam String path) {

        fileService.deleteFile(path);

        return ResponseEntity.ok("File deleted successfully");
    }

    @GetMapping("/download")
    public ResponseEntity<Resource> downloadFile(
            @RequestParam String path) {

        return fileService.download(path);
    }

}
