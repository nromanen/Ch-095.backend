package com.softserve.academy.event.controller;

import com.softserve.academy.event.entity.PhotoInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletContext;
import java.io.File;
import java.io.InputStream;
import java.util.Properties;

@Api(value = "Uploading photo")
@RestController
public class FileUploadController {

    @Autowired
    ServletContext context;

    @ApiOperation(value = "Upload a picture to the server")
    @PostMapping(value = "/fileupload", headers=("content-type=multipart/*"))
    public ResponseEntity<PhotoInfo> upload(@RequestParam("file") MultipartFile inputFile) {
        PhotoInfo photoInfo = new PhotoInfo();
        HttpHeaders headers = new HttpHeaders();
        Properties properties = new Properties();
        if (!inputFile.isEmpty()) {
            try (InputStream propertiesFile = FileUploadController.class.getClassLoader().getResourceAsStream("application.properties")) {
                properties.load(propertiesFile);
                String originalFilename = inputFile.getOriginalFilename();
                File destinationFile = new File(properties.getProperty("image.upload.dir") + File.separator + originalFilename);
                inputFile.transferTo(destinationFile);
                photoInfo.setFileName(destinationFile.getPath());
                headers.add("File Uploaded Successfully - ", originalFilename);
                return new ResponseEntity<PhotoInfo>(photoInfo, headers, HttpStatus.OK);
            } catch (Exception e) {
                return new ResponseEntity<PhotoInfo>(HttpStatus.BAD_REQUEST);
            }
        }else{
            return new ResponseEntity<PhotoInfo>(HttpStatus.BAD_REQUEST);
        }
    }
}


