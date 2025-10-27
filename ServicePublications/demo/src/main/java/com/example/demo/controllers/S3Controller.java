package com.example.demo.controllers;

import com.example.demo.DTO.urlPerfilDTO;
import com.example.demo.Service.IS3Service.IS3Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.services.s3.model.Bucket;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Duration;
import java.util.List;

@RestController
@RequestMapping("s3")
@CrossOrigin(origins = {"http://localhost:5173", "http://localhost:3000", "http://localhost:8080"})
public class S3Controller {
    @Value("${spring.destination.folder}")
    private String destinationFolder;

    @Autowired
    private IS3Service s3Service;

    @PostMapping("/create")
    public ResponseEntity<String> createBucket(@RequestParam String bucketName){
        return ResponseEntity.ok(this.s3Service.createBucket(bucketName));
    }

    @GetMapping("/check/{bucketName}")
    public ResponseEntity<String> checkBucket(@PathVariable String bucketName){
        return ResponseEntity.ok(this.s3Service.checkIfBucketExist(bucketName));
    }

    @GetMapping("/list")
    public ResponseEntity<List<String>> listBuckets(){
        return ResponseEntity.ok(this.s3Service.getAllBuckets());
    }

    @PostMapping("/upload")
    public ResponseEntity<String> uploadFile(@RequestParam String bucketName, @RequestParam String key, @RequestPart MultipartFile file) throws IOException {
        try {
            Path staticDir = Paths.get(destinationFolder);
            if (!Files.exists(staticDir)) {
                Files.createDirectories(staticDir);
            }

            Path filePath = staticDir.resolve(file.getOriginalFilename());
            Path finalPath = Files.write(filePath, file.getBytes());

            Boolean result = this.s3Service.uploadFile(bucketName, key, finalPath);

            if(result){
                Files.delete(filePath);
                return ResponseEntity.ok("Archivo cargado correctamente");
            } else {
                return ResponseEntity.internalServerError().body("Error al cargar el archivo al bucket");
            }
        } catch (IOException e) {
            throw new IOException("Error al procesar el archivo.");
        }
    }

    @PostMapping("/download")
    public ResponseEntity<String> downloadFile(@RequestParam String bucketName, @RequestParam String key) throws IOException {
        this.s3Service.downloadFile(bucketName, key);
        return ResponseEntity.ok("Archivo descargado correctamente");
    }

    @PostMapping("/upload/presigned")
    public ResponseEntity<String> generatePresignedUploadUrl(@RequestParam String bucketName, @RequestParam String key, @RequestParam Long time){
        Duration durationToLive = Duration.ofMinutes(time);
        return ResponseEntity.ok(this.s3Service.generatePresignedUploadUrl(bucketName, key, durationToLive));
    }

    @PostMapping("/download/presigned")
    public ResponseEntity<urlPerfilDTO> generatePresignedDownloadUrl(@RequestBody urlPerfilDTO dto){

        String presignedUrl = s3Service.generatePresignedDownloadUrl(
                "ecotips",
                "users/"+dto.getUrlkey(),
                Duration.ofMinutes(10)
        );
        urlPerfilDTO url = new urlPerfilDTO(
                presignedUrl
        );

        return ResponseEntity.ok(url);
    }

    // Ejemplo en tu controlador dddd
    @PostMapping("/files/presigned-upload")
    public ResponseEntity<String> createUploadUrl(@RequestParam String fileName) {
        String bucketName = "ecotips";
        // puedes agregar carpetas por usuario, por ejemplo
        String key = "users/" + fileName;

        String presignedUrl = s3Service.generatePresignedUploadUrl(bucketName, key, Duration.ofMinutes(15));
        return ResponseEntity.ok(presignedUrl);
    }
    @GetMapping("/files/{fileKey}/presigned-download")
    public ResponseEntity<String> getPresignedDownloadUrl(@PathVariable String fileKey) {
        String bucketName = "ecotips";
        String url = s3Service.generatePresignedDownloadUrl(bucketName, fileKey, Duration.ofMinutes(5));
        return ResponseEntity.ok(url);
    }
    @GetMapping("/test-presigned")
    public ResponseEntity<String> testPresigned() {
        String url = s3Service.generatePresignedDownloadUrl(
                "ecotips",
                "users/publicaciones/1759268308222-ChatGPT Image 2 sept 2025, 01_42_08 p.m..png",
                Duration.ofMinutes(10)
        );
        return ResponseEntity.ok(url);
    }

}
