package com.example.recruitmenttask.service;

import com.example.recruitmenttask.model.Photo;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface PhotoService {
    void save(Photo photo, MultipartFile file);
    void delete(Long id);
    List<Photo> getAllPhotos();
}
