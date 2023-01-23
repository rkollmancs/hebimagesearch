package com.example.demo.implementation;

import lombok.extern.slf4j.Slf4j;
import com.example.demo.clients.S3Client;
import com.example.demo.clients.VisionClient;
import com.example.demo.dto.ImageDto;
import com.example.demo.entity.DetectedObject;
import com.example.demo.entity.Image;
import com.example.demo.exceptions.ClientException;
import com.example.demo.exceptions.ServerException;
import com.example.demo.repository.DetectedObjectRepository;
import com.example.demo.repository.ImageRepository;
import com.example.demo.service.ImageService;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class ImageServiceImpl implements ImageService {

    VisionClient visionClient;

    S3Client s3Client;

    ImageRepository imageRepository;

    DetectedObjectRepository detectedObjectRepository;

    public ImageServiceImpl(VisionClient visionClient, S3Client s3Client, ImageRepository imageRepository, DetectedObjectRepository detectedObjectRepository){
        this.visionClient = visionClient;
        this.s3Client = s3Client;
        this.imageRepository = imageRepository;
        this.detectedObjectRepository = detectedObjectRepository;
    }

    public List<Image> getImages(String searchString){
        List<Integer> detectedObjectImageIds = new ArrayList<>();
        if(StringUtils.isEmpty(searchString)){
            return imageRepository.findAll();
        }
        String[] searchStrings = searchString.split(",");
        detectedObjectImageIds = detectedObjectRepository.findAllDetectedObjectImageIdsByDescription(searchStrings[0]);
        if(detectedObjectImageIds.size()<1) return new ArrayList<>();
        for(int i = 1; i< searchStrings.length; i++){
            detectedObjectImageIds = detectedObjectRepository.findAllImageIdsByDescriptionAndImageIds(searchStrings[0],detectedObjectImageIds);
            if(detectedObjectImageIds.size()<1) return new ArrayList<>();
        }
        List<Image> images = imageRepository.findAllById(detectedObjectImageIds);
        return images;
    }
    public Image getImage(int imageId) throws ClientException {
        return imageRepository.findById(imageId).orElseThrow(()->new ClientException("Could not find image by provided ID"));
    }
    public Image saveImage(ImageDto imageDto) throws ClientException, ServerException, IOException {
        Image image = new Image();
        if(StringUtils.isEmpty(imageDto.getUrl())){
            if(imageDto.getFile()==null){ throw new ClientException("No image included");}
            try {
                String url = s3Client.uploadFile(imageDto.getFile());
                image.setUrl(url);
            } catch (Exception ex){
                throw new ServerException("Could not upload image");
            }
        } else {
            image.setUrl(imageDto.getUrl());
        }

        image.setLabel(imageDto.getLabel());
        image = imageRepository.save(image);
        boolean generateLabel = StringUtils.isEmpty(image.getLabel());
        if(imageDto.getEnableObjectDetection()||generateLabel){
            List<String> objectDetectionStrings = visionClient.scanImage(image.getUrl());
            if(generateLabel){
                image.setLabel(String.join(",",objectDetectionStrings));
            }
            for(String description: objectDetectionStrings){
                DetectedObject detectedObject = new DetectedObject();
                detectedObject.setImageId(image.getLabelId());
                detectedObject.setDescription(description);
                detectedObjectRepository.save(detectedObject);
            }
        }
        return image;
    }

}
