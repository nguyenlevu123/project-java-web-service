//Xử lý lưu trữ file CV PDF trên máy chủ cục bộ.
package com.example.worksphere.service;

import com.example.worksphere.dto.response.CvUploadResponse;
import com.example.worksphere.exception.BadRequestException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Locale;
import java.util.UUID;

//Cung cấp chức năng tải lên CV dạng PDF.
@Service
public class FileStorageService {

    private static final long MAX_FILE_SIZE = 15L * 1024 * 1024;
    private static final String PDF_CONTENT_TYPE = "application/pdf";
    private static final String CV_UPLOAD_DIR = "uploads/cv";

    //Lưu file CV PDF vào thư mục uploads/cv.
    public CvUploadResponse uploadCv(MultipartFile file) {
        validateCvFile(file);

        String fileName = UUID.randomUUID() + ".pdf";
        Path uploadPath = Paths.get(CV_UPLOAD_DIR).toAbsolutePath().normalize();
        Path targetPath = uploadPath.resolve(fileName);

        try {
            Files.createDirectories(uploadPath);
            Files.copy(file.getInputStream(), targetPath, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException exception) {
            throw new BadRequestException("Could not store CV file");
        }

        return CvUploadResponse.builder()
                .fileName(fileName)
                .fileUrl("/uploads/cv/" + fileName)
                .fileSize(file.getSize())
                .contentType(file.getContentType())
                .build();
    }

    //Kiểm tra file tải lên đúng định dạng và kích thước cho phép.
    private void validateCvFile(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            throw new BadRequestException("CV file is required");
        }

        if (file.getSize() > MAX_FILE_SIZE) {
            throw new BadRequestException("CV file must not exceed 15MB");
        }

        String contentType = file.getContentType();
        if (!PDF_CONTENT_TYPE.equalsIgnoreCase(contentType)) {
            throw new BadRequestException("CV file must be a PDF");
        }

        String originalFilename = file.getOriginalFilename();
        if (originalFilename == null || !originalFilename.toLowerCase(Locale.ROOT).endsWith(".pdf")) {
            throw new BadRequestException("CV filename must end with .pdf");
        }
    }
}
