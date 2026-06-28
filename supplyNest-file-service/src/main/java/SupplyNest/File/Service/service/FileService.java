package SupplyNest.File.Service.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.Normalizer;
import java.util.Objects;
import java.util.UUID;

@Slf4j
@Service
public class FileService {

    private final Path uploadRoot = Paths.get("storage/uploads");

    public String saveFile(MultipartFile file, String folder) {

        try {

            if (file == null || file.isEmpty()) {
                throw new RuntimeException("File is empty");
            }

            Files.createDirectories(uploadRoot);

            Path folderPath = uploadRoot.resolve(folder).normalize();

            if (!folderPath.startsWith(uploadRoot.toAbsolutePath().normalize())) {
                throw new RuntimeException("Invalid folder path");
            }

            Files.createDirectories(folderPath);

            String originalName = Objects.requireNonNull(file.getOriginalFilename());

            originalName = Normalizer.normalize(originalName, Normalizer.Form.NFKC);
            originalName = originalName.replaceAll("[^a-zA-Z0-9._-]", "_");
            originalName = originalName.replaceAll("_+", "_");

            String fileName = UUID.randomUUID() + "_" + originalName;

            Path destination = folderPath.resolve(fileName);

            file.transferTo(destination);

            return "uploads/" +
                    uploadRoot.relativize(destination)
                            .toString()
                            .replace("\\", "/");

        } catch (IOException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    public void deleteFile(String relativePath) {

        try {

            if (relativePath.startsWith("uploads/")) {
                relativePath = relativePath.substring(8);
            }

            Path file = uploadRoot.resolve(relativePath).normalize();

            Files.deleteIfExists(file);

        } catch (IOException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    public ResponseEntity<Resource> download(String relativePath) {

        try {

            if (relativePath.startsWith("uploads/")) {
                relativePath = relativePath.substring(8);
            }

            Path file = uploadRoot.resolve(relativePath).normalize();

            Resource resource = new UrlResource(file.toUri());

            if (!resource.exists()) {
                throw new RuntimeException("File not found");
            }

            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION,
                            ContentDisposition.attachment()
                                    .filename(file.getFileName().toString())
                                    .build()
                                    .toString())
                    .body(resource);

        } catch (MalformedURLException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

}
