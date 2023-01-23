package com.example.demo.controller;

import lombok.extern.slf4j.Slf4j;
import com.example.demo.dto.ImageDto;
import com.example.demo.entity.Image;
import com.example.demo.exceptions.ClientException;
import com.example.demo.exceptions.ServerException;
import com.example.demo.service.ImageService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Slf4j
public class ImageController {

    ImageService imageService;

    public ImageController(ImageService imageService){
        this.imageService = imageService;
    }

    @GetMapping("/images")
    public ResponseEntity<?> getImages(@RequestParam String searchString){
        List<Image> images = imageService.getImages(searchString);
        return new ResponseEntity<>(images, HttpStatus.OK);
    }

    @GetMapping("/images/{imageId}")
    public ResponseEntity<?> getImage(@PathVariable int imageId) throws Exception {
        Image image;
        try {
            image = imageService.getImage(imageId);
        } catch (ClientException ex){
            return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity(image, HttpStatus.OK);
    }

    @PostMapping("/images")
    public ResponseEntity<?> postImage(@RequestBody ImageDto imageDto){
        Image image;
        try {
             image = imageService.saveImage(imageDto);
        } catch (ClientException ex){
            return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (ServerException ex){
            return new ResponseEntity<>(ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return new ResponseEntity<>(image, HttpStatus.OK);
    }
}
