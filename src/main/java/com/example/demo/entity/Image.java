package com.example.demo.entity;

import lombok.Data;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Data
@Table(name = "Images")
public class Image {

    @Id
    @Column(name = "LabelID", unique = true, nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer labelId;

    @Column(name = "URL", columnDefinition = "NVARCHAR(2083)", nullable = false)
    String url;

    @Column(name = "Label", columnDefinition = "NVARCHAR(500)", nullable = false)
    String label;

    //Expandable fields: Uploaded date, Last modified date, Uploaded by IP

    @OneToMany(cascade = CascadeType.REMOVE)
    @JoinColumn(name = "LabelID")
    private List<DetectedObject> detectedObjects = new ArrayList<>();
}
