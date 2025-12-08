package gguip1.community.domain.image.service;

import gguip1.community.domain.image.dto.PresignedImageUploadResponse;
import gguip1.community.domain.image.entity.Image;
import gguip1.community.domain.image.repository.ImageRepository;
import gguip1.community.global.exception.ErrorCode;
import gguip1.community.global.exception.ErrorException;
import gguip1.community.global.infra.S3Manager;
import gguip1.community.global.infra.dto.PresignedUrlResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ImageService {
    private final ImageRepository imageRepository;
    private final S3Manager s3Manager;

    public PresignedImageUploadResponse getPresignedUrl(String originalFilename) {
        String s3AccessKey = createUniqueFilename("profile", originalFilename);
        Image image = imageRepository.save(
                Image.builder()
                        .s3_key(s3AccessKey)
                        .build()
        );

        PresignedUrlResponse s3Response = s3Manager.getPresignedUrl(s3AccessKey);

        return new PresignedImageUploadResponse(
            s3Response.presignedUrl(),
            s3Response.key(),
            image.getImageId()
        );
    }

    public List<PresignedImageUploadResponse> getMultiplePresignedUrls(List<String> originalFilenames) {
        if (originalFilenames.size() > 5) {
            throw new ErrorException(ErrorCode.TOO_MANY_IMAGES);
        }

        List<Image> imagesToSave = originalFilenames.stream()
                .map(filename -> {
                    String s3AccessKey = createUniqueFilename("post", filename);
                    return Image.builder()
                            .s3_key(s3AccessKey)
                            .build();
                })
                .toList();

        List<Image> savedImages = imageRepository.saveAll(imagesToSave);

        return savedImages.stream()
                .map(image -> {
                    PresignedUrlResponse s3Response = s3Manager.getPresignedUrl(image.getS3_key());
                    return new PresignedImageUploadResponse(
                            s3Response.presignedUrl(),
                            s3Response.key(),
                            image.getImageId()
                    );
                })
                .toList();
    }

    private String createUniqueFilename(String prefix, String originalFileName) {
        return String.format("%s/original/%s_%s", prefix, UUID.randomUUID(), originalFileName);
    }
}
