package com.softserve.academy.event.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@RestController
public class FileUploadController {

    public static String uploadDirectory = "src\\uploads";

    @GetMapping(value = "/upload")
    public String UploadPage(Model model) {
        return "uploadview";
    }

    @PostMapping(value = "/upload",consumes = {"multipart/form-data"})
    public String upload(Model model,@RequestParam("files") MultipartFile[] files) {
        StringBuilder fileNames = new StringBuilder();
        for (MultipartFile file : files) {
            Path fileNameAndPath = Paths.get(uploadDirectory, file.getOriginalFilename());
            fileNames.append(file.getOriginalFilename()).append(" ");
            try {
                Files.write(fileNameAndPath, file.getBytes());

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        model.addAttribute("msg", "Successfully uploaded files "+fileNames.toString());
        return "uploadstatusview";
    }


}

