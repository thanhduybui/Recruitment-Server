package com.edu.hcmute.service;


import com.edu.hcmute.response.ServiceResponse;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface FileService {
   String uploadFile(MultipartFile multipartFile, String fileName) throws IOException;
   String getExtension(String fileName);
}
