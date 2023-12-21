package com.edu.hcmute.service;


import com.google.auth.Credentials;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.storage.BlobId;
import com.google.cloud.storage.BlobInfo;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.Base64;

@Service
public class FileService {
    private static final String BUCKET_NAME = "jobhunt-b08b1.appspot.com";
    private static final String CREDENTIAL_PATH = "src/main/resources/firebase.json";
    public String uploadFile(MultipartFile multipartFile, String fileName) throws IOException {
        try (ByteArrayInputStream inputStream = new ByteArrayInputStream(multipartFile.getBytes())) {
            BlobId blobId = BlobId.of(BUCKET_NAME, fileName);
            BlobInfo blobInfo = BlobInfo.newBuilder(blobId)
                    .setContentType(multipartFile.getContentType())
                    .build();

            // Load Firebase credentials from the service account JSON file
            GoogleCredentials credentials = GoogleCredentials.fromStream(new FileInputStream(CREDENTIAL_PATH));

            Storage storage = StorageOptions.newBuilder().setCredentials(credentials).build().getService();
            storage.create(blobInfo, inputStream);

            return String.format("https://firebasestorage.googleapis.com/v0/b/%s/o/%s?alt=media",
                    BUCKET_NAME, URLEncoder.encode(fileName, StandardCharsets.UTF_8));
        }
    }

    public String getExtension(String fileName) {
        return fileName.substring(fileName.lastIndexOf("."));
    }
}