package gguip1.community.domain.image.controller;

import gguip1.community.domain.image.dto.ImageResponse;
import gguip1.community.domain.image.dto.PresignedImageUploadResponse;
import gguip1.community.domain.image.service.ImageService;
import gguip1.community.global.infra.dto.PresignedUrlRequest;
import gguip1.community.global.infra.dto.PresignedUrlResponse;
import gguip1.community.global.response.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequiredArgsConstructor
public class ImageController {
    private final ImageService imageService;

        @PostMapping("/images/{type}/presigned-url")
        public ResponseEntity<ApiResponse<PresignedImageUploadResponse>> getPresignedUrl(
                @PathVariable("type") String type,
                @RequestBody PresignedUrlRequest presignedUrlRequest) {
            return ResponseEntity.status(HttpStatus.OK).body(
                ApiResponse.success(
                        "presigned_url_created",
                        imageService.getPresignedUrl(type, presignedUrlRequest.originalFilename())
                )
            );
        }
}
