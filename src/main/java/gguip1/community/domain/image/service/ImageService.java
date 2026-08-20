package gguip1.community.domain.image.service;

import gguip1.community.domain.image.dto.ImageUploadResponse;
import gguip1.community.domain.image.entity.Image;
import gguip1.community.domain.image.repository.ImageRepository;
import gguip1.community.domain.image.storage.ImageStorage;
import gguip1.community.domain.image.storage.ImageUpload;
import gguip1.community.domain.image.storage.StoredImage;
import gguip1.community.global.exception.ErrorCode;
import gguip1.community.global.exception.ErrorException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class ImageService {
    private static final Map<String, byte[]> IMAGE_SIGNATURES = Map.of(
            "image/jpeg", new byte[]{(byte) 0xFF, (byte) 0xD8, (byte) 0xFF},
            "image/png", new byte[]{(byte) 0x89, 0x50, 0x4E, 0x47},
            "image/gif", new byte[]{0x47, 0x49, 0x46},
            "image/webp", new byte[]{0x52, 0x49, 0x46, 0x46}
    );

    private final ImageRepository imageRepository;
    private final ImageStorage imageStorage;

    @Value("${app.image.max-file-size-bytes:5242880}")
    private long maxFileSizeBytes;

    public ImageUploadResponse uploadProfile(MultipartFile file) {
        return upload("profile", file);
    }

    public List<ImageUploadResponse> uploadPosts(List<MultipartFile> files) {
        if (files == null || files.isEmpty()) {
            throw new ErrorException(ErrorCode.NOT_FOUND);
        }
        if (files.size() > 5) {
            throw new ErrorException(ErrorCode.TOO_MANY_IMAGES);
        }
        return files.stream().map(file -> upload("post", file)).toList();
    }

    private ImageUploadResponse upload(String category, MultipartFile file) {
        ImageUpload upload = validate(file);
        StoredImage storedImage = imageStorage.store(category, upload);
        try {
            Image image = imageRepository.save(Image.builder().storageKey(storedImage.storageKey()).build());
            return new ImageUploadResponse(image.getImageId(), storedImage.storageKey(), storedImage.publicUrl());
        } catch (RuntimeException exception) {
            imageStorage.delete(storedImage.storageKey());
            throw exception;
        }
    }

    private ImageUpload validate(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            throw new IllegalArgumentException("Image file is required");
        }
        if (file.getSize() > maxFileSizeBytes) {
            throw new IllegalArgumentException("Image file exceeds the maximum size");
        }

        String contentType = file.getContentType() == null ? "" : file.getContentType().toLowerCase();
        byte[] expectedSignature = IMAGE_SIGNATURES.get(contentType);
        if (expectedSignature == null) {
            throw new IllegalArgumentException("Unsupported image content type");
        }

        try {
            byte[] content = file.getBytes();
            if (!hasExpectedSignature(contentType, content, expectedSignature)) {
                throw new IllegalArgumentException("Image content does not match its content type");
            }
            return new ImageUpload(content, contentType);
        } catch (IOException exception) {
            throw new IllegalArgumentException("Unable to read image file", exception);
        }
    }

    private boolean hasExpectedSignature(String contentType, byte[] content, byte[] signature) {
        if (content.length < signature.length) {
            return false;
        }
        for (int index = 0; index < signature.length; index++) {
            if (content[index] != signature[index]) {
                return false;
            }
        }
        if (!"image/webp".equals(contentType)) {
            return true;
        }
        return content.length >= 12
                && content[8] == 0x57
                && content[9] == 0x45
                && content[10] == 0x42
                && content[11] == 0x50;
    }
}
