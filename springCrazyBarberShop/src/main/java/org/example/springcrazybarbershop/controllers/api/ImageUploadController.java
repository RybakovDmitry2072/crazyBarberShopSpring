package org.example.springcrazybarbershop.controllers.api;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.springcrazybarbershop.dto.form.FileForm;
import org.example.springcrazybarbershop.models.File;
import org.example.springcrazybarbershop.services.interfeces.IFileService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@Slf4j
public class ImageUploadController {
    private final IFileService fileService;

    @PostMapping("api/users/{userId}/upload-image")
    public ResponseEntity<?> uploadImage(@RequestParam("file") MultipartFile multipartFile,
                                         @PathVariable Long userId,
                                         @RequestParam(defaultValue = "user_uploads") String directory) throws IOException {

        log.info("multipartFile:" + multipartFile);
        FileForm fileForm = FileForm.builder()
                .fileName(multipartFile.getOriginalFilename())
                .contentType(multipartFile.getContentType())
                .size(multipartFile.getSize())
                .userId(userId)
                .fileStream(multipartFile.getInputStream())
                .build();
        log.info("FILEFORM:" + fileForm);

        File file = fileService.uploadFile(fileForm, directory);
        log.info("FILE: " + file);
        return ResponseEntity.ok(Map.of(
                "status", "success",
                "message", "File uploaded successfully",
                "storageFileName", file.getStorageFileName()
        ));
    }

}
