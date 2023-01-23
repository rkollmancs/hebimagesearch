package com.example.demo.clients;

import com.example.demo.exceptions.ServerException;
import com.google.cloud.spring.vision.CloudVisionTemplate;
import com.google.cloud.vision.v1.AnnotateImageResponse;
import com.google.cloud.vision.v1.EntityAnnotation;
import com.google.cloud.vision.v1.Feature.Type;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;

@Service
public class VisionClient {

    ResourceLoader resourceLoader;

    CloudVisionTemplate cloudVisionTemplate;

    @Autowired
    public VisionClient(ResourceLoader resourceLoader, CloudVisionTemplate cloudVisionTemplate){
        this.resourceLoader = resourceLoader;
        this.cloudVisionTemplate = cloudVisionTemplate;
    }

    @PostConstruct
    public void init(){

    }

    public List<String> scanImage(String url) throws ServerException {
        AnnotateImageResponse res = this.cloudVisionTemplate.analyzeImage(this.resourceLoader.getResource(url), Type.LABEL_DETECTION);

        List<String> recognizedObjects = new ArrayList<>();

        if (res.hasError()) {
            System.out.format("Error: %s%n", res.getError().getMessage());
            throw new ServerException(res.getError().getMessage());
        }

        for(EntityAnnotation entity : res.getLabelAnnotationsList()){
            if(entity.getScore()>.8){
                recognizedObjects.add(entity.getDescription());
            }
        }

        return recognizedObjects;
    }

    public String getLabel(String url){
        AnnotateImageResponse res = this.cloudVisionTemplate.analyzeImage(this.resourceLoader.getResource(url), Type.LABEL_DETECTION);
        return res.getLabelAnnotationsList().toString();
    }
}
