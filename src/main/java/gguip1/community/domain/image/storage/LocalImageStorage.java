package gguip1.community.domain.image.storage;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.Map;
import java.util.UUID;

@Component
public class LocalImageStorage implements ImageStorage {
    private static final Map<String, String> EXTENSIONS = Map.of(
            "image/jpeg", "jpg",
            "image/png", "png",
            "image/gif", "gif",
            "image/webp", "webp"
    );

    private final Path rootDirectory;
    private final String publicPrefix;

    public LocalImageStorage(
            @Value("${app.image.storage.local-root:/data/uploads}") String localRoot,
            @Value("${app.image.storage.public-prefix:/uploads}") String publicPrefix
    ) {
        this.rootDirectory = Path.of(localRoot).toAbsolutePath().normalize();
        this.publicPrefix = publicPrefix.replaceAll("/+$", "");
    }

    @Override
    public StoredImage store(String category, ImageUpload upload) {
        String extension = EXTENSIONS.get(upload.contentType());
        if (extension == null) {
            throw new IllegalArgumentException("Unsupported image content type");
        }

        String storageKey = category + "/" + UUID.randomUUID() + "." + extension;
        Path destination = rootDirectory.resolve(storageKey).normalize();
        if (!destination.startsWith(rootDirectory)) {
            throw new IllegalStateException("Invalid image storage path");
        }

        try {
            Files.createDirectories(destination.getParent());
            Files.write(destination, upload.content(), StandardOpenOption.CREATE_NEW);
            return new StoredImage(storageKey, publicUrl(storageKey));
        } catch (IOException exception) {
            throw new IllegalStateException("Failed to store image", exception);
        }
    }

    @Override
    public void delete(String storageKey) {
        if (storageKey == null || storageKey.isBlank()) {
            return;
        }
        Path destination = rootDirectory.resolve(storageKey).normalize();
        if (!destination.startsWith(rootDirectory)) {
            throw new IllegalArgumentException("Invalid image storage path");
        }
        try {
            Files.deleteIfExists(destination);
        } catch (IOException exception) {
            throw new IllegalStateException("Failed to delete image", exception);
        }
    }

    @Override
    public String publicUrl(String storageKey) {
        if (storageKey == null || storageKey.isBlank()) {
            return null;
        }
        return publicPrefix + "/" + storageKey;
    }
}
