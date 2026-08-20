package gguip1.community.domain.image.controller;

import gguip1.community.domain.image.dto.ImageUploadResponse;
import gguip1.community.domain.image.service.ImageService;
import gguip1.community.global.auth.annotation.Auth;
import gguip1.community.global.response.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class ImageController {
    private final ImageService imageService;

    @Auth
    @PostMapping(value = "/images/profile", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ApiResponse<ImageUploadResponse>> uploadProfile(@RequestPart("file") MultipartFile file) {
        return ResponseEntity.ok(ApiResponse.success("image_uploaded", imageService.uploadProfile(file)));
    }

    @Auth
    @PostMapping(value = "/images/posts", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ApiResponse<List<ImageUploadResponse>>> uploadPosts(@RequestPart("files") List<MultipartFile> files) {
        return ResponseEntity.ok(ApiResponse.success("images_uploaded", imageService.uploadPosts(files)));
    }
}
