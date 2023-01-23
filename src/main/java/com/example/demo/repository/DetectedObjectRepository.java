package com.example.demo.repository;

import com.example.demo.entity.DetectedObject;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface DetectedObjectRepository extends JpaRepository<DetectedObject, Integer> {
    @Query(value = "select imageId from DetectedObject where description = :searchString")
    List<Integer> findAllDetectedObjectImageIdsByDescription(String searchString);

    @Query(value = "select imageId from DetectedObject where imageId in (:imageIds) and description = :searchString")
    List<Integer> findAllImageIdsByDescriptionAndImageIds(String searchString, List<Integer> imageIds);
}
