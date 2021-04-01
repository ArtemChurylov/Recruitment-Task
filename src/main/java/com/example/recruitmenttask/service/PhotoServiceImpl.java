package com.example.recruitmenttask.service;

import com.example.recruitmenttask.model.Photo;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

@Service
public class PhotoServiceImpl implements PhotoService {

    private final PhotoRepository photoRepository;

    public static String originalFilesDirectory = System.getProperty("user.dir") + "/src/main/resources/static/original/";
    public static String scaledFilesDirectory = System.getProperty("user.dir") + "/src/main/resources/static/scaled/";

    public PhotoServiceImpl(PhotoRepository photoRepository) {
        this.photoRepository = photoRepository;
    }

    @Override
    public void save(Photo photo, MultipartFile multipartFile) {
        if (multipartFile.getOriginalFilename().endsWith(".jpg") || multipartFile.getOriginalFilename().endsWith(".png")) {
            //Get path for original file
            Path originalFilePath = Paths.get(originalFilesDirectory, multipartFile.getOriginalFilename());
            //Get path for resized file
            Path scaledFilePath = Paths.get(scaledFilesDirectory, multipartFile.getOriginalFilename());

            photo.setFileName(multipartFile.getOriginalFilename());

            try {
                //Write an original file
                Files.write(originalFilePath, multipartFile.getBytes());

                //Convert a multipartFile to File for working with BufferedImage
                File convFile = new File(String.valueOf(scaledFilePath));
                multipartFile.transferTo(convFile);

                //Get original file
                BufferedImage originalImage = ImageIO.read(convFile);
                Files.delete(Path.of(String.valueOf(scaledFilePath)));

                //Create resized file
                BufferedImage resizedImage = new BufferedImage(300, 300, originalImage.getType());
                Graphics2D graphics2D = resizedImage.createGraphics();
                graphics2D.drawImage(originalImage, 0, 0, 300, 300, null);
                graphics2D.dispose();

                //Write a resized file
                ImageIO.write(resizedImage, "png", convFile);
            } catch (IOException e) {
                e.printStackTrace();
            }
            photoRepository.save(photo);
        } else throw new IllegalStateException("Invalid file type");
    }

    @Override
    public void delete(Long id) {
        Photo photo = photoRepository.findById(id).orElseThrow();
        try {
            Files.delete(Path.of(originalFilesDirectory+photo.getFileName()));
            Files.delete(Path.of(scaledFilesDirectory+photo.getFileName()));
        } catch (IOException e) {
            e.printStackTrace();
        }
        photoRepository.deleteById(id);
    }

    @Override
    public List<Photo> getAllPhotos() {
        return photoRepository.findAll();
    }
}
