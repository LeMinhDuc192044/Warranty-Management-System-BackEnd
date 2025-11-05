package com.warrantyclaim.warrantyclaim_api.service;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface ImageUploadService {
    public String uploadImage(MultipartFile file) throws IOException;
    public void deleteImage(String imageUrl) throws IOException;
}
