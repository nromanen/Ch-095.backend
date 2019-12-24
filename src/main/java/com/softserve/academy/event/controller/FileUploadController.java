package com.softserve.academy.event.controller;

import com.softserve.academy.event.entity.PhotoInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletContext;
import java.io.File;
import java.io.InputStream;
import java.util.List;
import java.util.Properties;

@Api(value = "Uploading photo")
@RestController
@CrossOrigin(origins = "http://localhost:4200")
public class FileUploadController {

    @Autowired
    ServletContext context;

    @ApiOperation(value = "Upload a picture to the server")
    @PostMapping(value = "fileupload", headers=("content-type=multipart/*"))
    public ResponseEntity upload(@RequestParam("file") List<MultipartFile> inputFiles) {
        Properties properties = new Properties();
        for(MultipartFile inputFile : inputFiles ) {
            if (!inputFile.isEmpty()) {
                try (InputStream propertiesFile = FileUploadController.class.getClassLoader().getResourceAsStream("application.properties")) {
                    properties.load(propertiesFile);
                    String originalFilename = inputFile.getOriginalFilename();
                    File destinationFile = new File(properties.getProperty("image.upload.dir") + File.separator + originalFilename);
                    inputFile.transferTo(destinationFile);
                } catch (Exception e) {
                    return new ResponseEntity(HttpStatus.BAD_REQUEST);
                }
            } else {
                return new ResponseEntity(HttpStatus.BAD_REQUEST);
            }
        }
        return new ResponseEntity(HttpStatus.OK);
    }
}


