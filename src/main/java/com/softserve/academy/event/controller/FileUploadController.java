package com.softserve.academy.event.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.InputStream;
import java.util.List;
import java.util.Properties;

@Api(value = "Uploading photo")
@RestController
@CrossOrigin(origins = "http://localhost:4200")
public class FileUploadController {

    private final long MAX_UPLOAD_SIZE = 2 * 1024 * 1024;  // 2 MB


    @ApiOperation(value = "Upload a picture to the server")
    @PostMapping(value = "pictureUpload", headers=("content-type=multipart/*"))
    public ResponseEntity upload(@RequestParam("file") List<MultipartFile> inputFiles) {
        Properties properties = new Properties();
        for(MultipartFile inputFile : inputFiles ) {
            if (isValidPhoto(inputFile)) {
                try (InputStream propertiesFile = FileUploadController.class.getClassLoader().getResourceAsStream("application.properties")) {
                    properties.load(propertiesFile);
                    String originalFilename = inputFile.getOriginalFilename();
                    File destinationFile = new File(properties.getProperty("image.upload.dir") + File.separator + originalFilename);
                    inputFile.transferTo(destinationFile);

                }
                catch (Exception ex) {
                    return new ResponseEntity(HttpStatus.BAD_REQUEST);
                }
            } else {
                return new ResponseEntity(HttpStatus.BAD_REQUEST);
            }
        }
        return new ResponseEntity(HttpStatus.OK);
    }

    private boolean isValidPhoto(MultipartFile file){
        return  file.getSize() < MAX_UPLOAD_SIZE && !file.isEmpty();
    }

}



