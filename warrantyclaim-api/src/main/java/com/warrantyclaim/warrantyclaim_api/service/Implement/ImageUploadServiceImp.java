package com.warrantyclaim.warrantyclaim_api.service.Implement;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.warrantyclaim.warrantyclaim_api.service.ImageUploadService;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ImageUploadServiceImp implements ImageUploadService {

    private final Cloudinary cloudinary;

    @Override
    public String uploadImage(MultipartFile file) throws IOException {
        // Validate file
        validateImage(file);

        // Generate unique filename
        String originalFilename = file.getOriginalFilename();
        String publicId = "vehicles/" + UUID.randomUUID().toString();

        // Upload to Cloudinary
        Map uploadResult = cloudinary.uploader().upload(
                file.getBytes(),
                ObjectUtils.asMap(
                        "public_id", publicId,
                        "folder", "vinfast_vehicles",
                        "resource_type", "image",
                        "width", 800,              // ← Direct params, not nested
                        "height", 600,             // ← Direct params
                        "crop", "limit",           // ← Direct params
                        "quality", "auto"

                )
        );

        // Return secure URL
        return (String) uploadResult.get("secure_url");
    }

    public void deleteImage(String imageUrl) throws IOException {
        if (imageUrl != null && imageUrl.contains("cloudinary")) {
            // Extract public_id from URL
            String publicId = extractPublicId(imageUrl);
            cloudinary.uploader().destroy(publicId, ObjectUtils.emptyMap());
        }
    }

    private String extractPublicId(String imageUrl) {
        // Example URL: https://res.cloudinary.com/demo/image/upload/v1234567/vinfast_vehicles/abc-123.jpg
        // Extract: vinfast_vehicles/abc-123

        String[] parts = imageUrl.split("/upload/");
        if (parts.length > 1) {
            String pathWithVersion = parts[1];
            // Remove version number (v1234567/)
            String path = pathWithVersion.replaceFirst("v\\d+/", "");
            // Remove file extension
            return path.substring(0, path.lastIndexOf('.'));
        }
        return null;
    }

    private void validateImage(MultipartFile file) {
        if (file.isEmpty()) {
            throw new IllegalArgumentException("File is empty");
        }

        // Check file size (max 5MB)
        long maxSize = 5 * 1024 * 1024; // 5MB
        if (file.getSize() > maxSize) {
            throw new IllegalArgumentException("File size must be less than 5MB");
        }

        // Check file type
        String contentType = file.getContentType();
        if (contentType == null || !contentType.startsWith("image/")) {
            throw new IllegalArgumentException("Only image files are allowed");
        }

        // Allowed types: jpg, jpeg, png, webp
        if (!contentType.equals("image/jpeg") &&
                !contentType.equals("image/jpg") &&
                !contentType.equals("image/png") &&
                !contentType.equals("image/webp")) {
            throw new IllegalArgumentException("Only JPEG, JPG, PNG, and WebP images are allowed");
        }
    }
}
