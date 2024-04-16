package com.edu.hcmute.service.file;


import com.edu.hcmute.response.ServiceResponse;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface FileService {

   static final Long MAX_FILE_SIZE = 10 * 1024 * 1024L;
   String uploadFile(MultipartFile multipartFile, String fileName) throws IOException;
   String getExtension(String fileName);
}
