package com.example.demo.entity;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Data
@Entity
@Table(name = "DetectedObject")
public class DetectedObject {
    @Id
    @Column(name = "DetectedObjectId", unique = true, nullable = false)
    private Integer detectedObjectId;

    @Column(name = "ImageId")
    private Integer imageId;

    @Column(name = "Description")
    private String description;


}
