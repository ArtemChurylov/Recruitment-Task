package com.example.recruitmenttask.controller;

import com.example.recruitmenttask.model.Photo;
import com.example.recruitmenttask.service.PhotoService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;

@Controller
public class MainController {

    private final PhotoService photoService;

    public MainController(PhotoService photoService) {
        this.photoService = photoService;
    }

    @GetMapping("/")
    public String home(Model model) {
        model.addAttribute("photos", photoService.getAllPhotos());
        return "homePage";
    }

    @GetMapping("/add")
    public String addPhotoPage(Photo photo) {
        return "addPhotoPage";
    }

    @PostMapping("/add")
    public String addPhoto(@Valid Photo photo, BindingResult result, @RequestParam("file") MultipartFile file) {

        if (result.hasErrors()) {
            return "addPhotoPage";
        }

        if (photoService.getAllPhotos().stream().anyMatch(photo1 -> photo1.getFileName().equals(file.getOriginalFilename()))) {
            return "redirect:/uniqueFileNameException";
        }

        photoService.save(photo, file);
        return "redirect:/";
    }

    @PostMapping("/delete/{id}")
    public String deletePhoto(@PathVariable Long id) {
        photoService.delete(id);
        return "redirect:/";
    }

    @GetMapping("/uniqueFileNameException")
    public String showException() {
        return "uniqueFileNameException";
    }
}
