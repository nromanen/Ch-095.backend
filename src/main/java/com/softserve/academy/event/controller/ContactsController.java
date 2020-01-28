package com.softserve.academy.event.controller;

import com.softserve.academy.event.dto.ContactDTO;
import com.softserve.academy.event.dto.ItemDTO;
import com.softserve.academy.event.entity.Contact;
import com.softserve.academy.event.service.db.ContactService;
import com.softserve.academy.event.service.mapper.ContactMapper;
import com.softserve.academy.event.util.CsvUtils;
import com.softserve.academy.event.util.Page;
import com.softserve.academy.event.util.Pageable;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.StringWriter;

@Api(value = "/contact")
@RestController
@RequestMapping("/contact")
public class ContactsController {

    private final ContactService service;
    private final ContactMapper mapper;

    public ContactsController(ContactService service, ContactMapper mapper) {
        this.service = service;
        this.mapper = mapper;
    }

    @ApiOperation(value = "Get pageable contacts")
    @GetMapping
    public ResponseEntity<Page<ContactDTO>> getContacts(Pageable pageable, @RequestParam(required = false) String filter) {
        return ResponseEntity.ok(
                mapper.toPageDTO(
                        service.findAllByPageableAndFilter(pageable, filter)
                )
        );
    }

    @ApiOperation(value = "Create contacts from csv file")
    @PostMapping("/import/scv")
    @ResponseStatus(HttpStatus.OK)
    public void importCsv(@RequestParam("file") MultipartFile file, @RequestParam("importNames") boolean importNames) throws IOException {
        service.saveAll(
                CsvUtils.read(Contact.class, CsvUtils.CONTACT_WITH_HEADER_SCHEMA,
                        file.getInputStream()), importNames);
    }

    @ApiOperation(value = "Create contacts from csv file")
    @GetMapping(value = "/export/scv")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<ItemDTO<String>> exportCsv() throws IOException {
        try (StringWriter writer = new StringWriter()) {
            CsvUtils.write(ContactDTO.class, CsvUtils.CONTACT_WITH_HEADER_SCHEMA,
                    writer, mapper.toListDTO(service.findAllByCurrentUser()));
            return ResponseEntity.ok(mapper.toItemDTO(writer.getBuffer().toString()));
        }
    }

    @ApiOperation(value = "Create contact")
    @PostMapping
    public ResponseEntity<ItemDTO<Long>> createContact(@RequestBody ContactDTO contactDTO) {
        return ResponseEntity.ok(
                mapper.toItemDTO(
                        service.save(contactDTO).getId()
                )
        );
    }

    @ApiOperation(value = "Edit contact")
    @PutMapping
    @ResponseStatus(HttpStatus.OK)
    public void updateContact(@RequestBody ContactDTO contactDTO) {
        service.update(contactDTO);
    }

    @ApiOperation(value = "Remove contact")
    @DeleteMapping
    @ResponseStatus(HttpStatus.OK)
    public void deleteContact(@RequestParam Long id) {
        service.delete(id);
    }

}
