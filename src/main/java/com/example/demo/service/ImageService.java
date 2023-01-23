package com.example.demo.service;

import com.example.demo.dto.ImageDto;
import com.example.demo.entity.Image;

import java.util.List;

public interface ImageService {
    public List<Image> getImages(String searchString);
    public Image getImage(int imageId) throws Exception;
    public Image saveImage(ImageDto image) throws Exception;
}
