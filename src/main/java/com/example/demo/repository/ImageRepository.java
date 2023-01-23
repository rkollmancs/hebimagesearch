package com.example.demo.repository;

import com.example.demo.entity.Image;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public interface ImageRepository extends JpaRepository<Image, Integer>{
}
