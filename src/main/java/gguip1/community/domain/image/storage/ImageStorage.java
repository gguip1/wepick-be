package gguip1.community.domain.image.storage;

public interface ImageStorage {
    StoredImage store(String category, ImageUpload upload);
    void delete(String storageKey);
    String publicUrl(String storageKey);
}
