package com.example.demo.dto;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class ImageDto {
    MultipartFile file;
    String url;
    String label;
    Boolean enableObjectDetection;
}
