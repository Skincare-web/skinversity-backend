package com.skinversity.backend.Services;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

@Service
public class CloudinaryService {
    private final Cloudinary cloudinary;
    @Value("${cloudinary.cloud_name}")
    String cloudName;

    @Value("${cloudinary.api_key}")
    String apiKey;

    @Value("${cloudinary.api_secret}")
    String apiSecret;

    public CloudinaryService() {
        this.cloudinary = new Cloudinary(ObjectUtils.asMap(
                "cloud_name", cloudName,
                "api_key", apiKey,
                "api_secret", apiSecret
        ));
    }

    public String uploadFile(MultipartFile file) throws IOException {
        try {
            Map<String, Object> uploadResult = cloudinary.uploader().upload(file.getBytes(), ObjectUtils.emptyMap());
            return (String) uploadResult.get("url");
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(cloudName + "\n" + apiKey + "\n" + apiSecret);
        }
        return null;
    }
}
