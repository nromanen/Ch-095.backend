package com.softserve.academy.event.controller;

import com.softserve.academy.event.dto.ContactDTO;
import com.softserve.academy.event.dto.ItemDTO;
import com.softserve.academy.event.entity.Contact;
import com.softserve.academy.event.entity.User;
import com.softserve.academy.event.service.db.ContactService;
import com.softserve.academy.event.service.mapper.ContactMapper;
import com.softserve.academy.event.util.CsvUtils;
import com.softserve.academy.event.util.Page;
import com.softserve.academy.event.util.Pageable;
import org.hibernate.TypeMismatchException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Files;

@RestController
@RequestMapping("/contact")
public class ContactsController {

    private final ContactService service;
    private final ContactMapper mapper;

    public ContactsController(ContactService service, ContactMapper mapper) {
        this.service = service;
        this.mapper = mapper;
    }

    @GetMapping
    public ResponseEntity<Page<ContactDTO>> getContacts(Pageable pageable, @RequestParam(required = false) String filter) {
        return ResponseEntity.ok(
                mapper.toPageDTO(
                        service.findAllByPageableAndFilter(pageable, filter)
                )
        );
    }

    @PostMapping("/import/scv")
    @ResponseStatus(HttpStatus.OK)
    public byte[] importScv(@RequestParam("file") MultipartFile file) throws IOException {
        if (true) return CsvUtils.write(service.findAll());
        if (!file.getContentType().contains("csv")) {
            throw new TypeMismatchException("Can't read file. File type must be '.csv'");
        }
        service.saveAll(CsvUtils.read(Contact.class, file.getInputStream()));
        return null;
    }

    @PostMapping
    public ResponseEntity<ItemDTO<Long>> createContact(@RequestBody ContactDTO contactDTO) {
        return ResponseEntity.ok(
                mapper.toItemDTO(
                        service.save(contactDTO).getId()
                )
        );
    }

    @PutMapping
    @ResponseStatus(HttpStatus.OK)
    public void updateContact(@RequestBody ContactDTO contactDTO) {
        service.update(contactDTO);
    }

    @DeleteMapping
    @ResponseStatus(HttpStatus.OK)
    public void deleteContact(@RequestParam Long id) {
        service.delete(id);
    }

}
