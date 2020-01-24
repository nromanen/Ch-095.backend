package com.softserve.academy.event.controller;

import com.softserve.academy.event.dto.ContactDTO;
import com.softserve.academy.event.dto.ItemDTO;
import com.softserve.academy.event.entity.Contact;
import com.softserve.academy.event.service.db.ContactService;
import com.softserve.academy.event.service.mapper.ContactMapper;
import com.softserve.academy.event.util.CsvUtils;
import com.softserve.academy.event.util.Page;
import com.softserve.academy.event.util.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

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
    public void importCsv(@RequestParam("file") MultipartFile file, @RequestParam("importNames") boolean importNames) throws IOException {
        service.saveAll(
                CsvUtils.read(Contact.class, CsvUtils.CONTACT_WITH_HEADER_SCHEMA,
                        file.getInputStream()), importNames);
    }

    @GetMapping(value = "/export/scv")
    @ResponseStatus(HttpStatus.OK)
    public void exportCsv(HttpServletResponse response) throws IOException {
        response.setContentType("application/csv");
        response.setHeader("Content-Disposition","attachment; filename=\"contacts.csv\"");
        try(ServletOutputStream outputStream = response.getOutputStream()){
            CsvUtils.write(ContactDTO.class, CsvUtils.CONTACT_WITH_HEADER_SCHEMA,
                    outputStream, mapper.toListDTO(service.findAll()));
            outputStream.flush();
        }
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
