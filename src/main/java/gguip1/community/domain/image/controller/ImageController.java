package gguip1.community.domain.image.controller;

import gguip1.community.domain.image.dto.PresignedImageUploadResponse;
import gguip1.community.domain.image.service.ImageService;
import gguip1.community.global.auth.annotation.Auth;
import gguip1.community.global.infra.dto.PresignedUrlRequest;
import gguip1.community.global.response.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class ImageController {
    private final ImageService imageService;

    @Auth
    @PostMapping("/images/profile/presigned-url")
    public ResponseEntity<ApiResponse<PresignedImageUploadResponse>> getPresignedUrl(
            @RequestBody PresignedUrlRequest presignedUrlRequest) {
        return ResponseEntity.status(HttpStatus.OK).body(
            ApiResponse.success(
                    "presigned_url_created",
                    imageService.getPresignedUrl(presignedUrlRequest.originalFilename())
            )
        );
    }

    @Auth
    @PostMapping("/images/post/presigned-urls")
    public ResponseEntity<ApiResponse<List<PresignedImageUploadResponse>>> getMultiplePresignedUrls(
            @RequestBody List<PresignedUrlRequest> presignedUrlRequest) {

        List<String> originalFilenames = presignedUrlRequest.stream()
                .map(PresignedUrlRequest::originalFilename)
                .toList();

        return ResponseEntity.status(HttpStatus.OK).body(
            ApiResponse.success(
                    "presigned_urls_created",
                    imageService.getMultiplePresignedUrls(originalFilenames)
            )
        );
    }
}
