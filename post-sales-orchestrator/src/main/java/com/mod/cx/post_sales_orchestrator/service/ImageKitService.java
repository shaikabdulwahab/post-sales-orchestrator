package com.mod.cx.post_sales_orchestrator.service;

import io.imagekit.sdk.ImageKit;
import io.imagekit.sdk.config.Configuration;
import io.imagekit.sdk.models.FileCreateRequest;
import io.imagekit.sdk.models.results.Result;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.Map;

@Service
public class ImageKitService {

    private final ImageKit imageKit;

    public ImageKitService(
            @Value("${imagekit.public-key}") String publicKey,
            @Value("${imagekit.private-key}") String privateKey,
            @Value("${imagekit.url-endpoint}") String urlEndpoint){
        this.imageKit = ImageKit.getInstance();
        Configuration config = new Configuration(publicKey, privateKey, urlEndpoint);
        imageKit.setConfig(config);
    }

    public String uploadAndGetUrl(MultipartFile file, String userId, String category) throws Exception {
        String folderPath = String.format("head/user_%s/%s", userId, category);

        FileCreateRequest request = new FileCreateRequest(file.getBytes(), file.getOriginalFilename());
        request.setFolder(folderPath);
        request.setUseUniqueFileName(true);
        request.setPrivateFile(true);

        Result result = imageKit.upload(request);
        
        // Immediately generate the secure URL after upload
        return generateSecureUrl(result.getFilePath());
    }

    public Map<String, String> uploadPrivateAsset(MultipartFile file, String userId, String category) throws Exception{

        String folderPath = String.format("head/user_%s/%s",userId,category);

        FileCreateRequest request = new FileCreateRequest(file.getBytes(), file.getOriginalFilename());
        request.setFolder(folderPath);
        request.setUseUniqueFileName(true);
        request.setPrivateFile(true);

        Result result = imageKit.upload(request);

        Map<String, String> response = new HashMap<>();
        response.put("filePath", result.getFilePath());
        response.put("fileId", result.getFileId());
        return response;
    }

    public String generateSecureUrl(String filePath) {
        Map<String, Object> options = new HashMap<>();
        options.put("path", filePath);
        options.put("signed", true);
        options.put("expireSeconds", 1800); // 30 mins expiry

        return imageKit.getUrl(options);
    }
}
