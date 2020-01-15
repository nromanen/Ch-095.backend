package com.softserve.academy.event.controller;

import com.softserve.academy.event.dto.ContactDTO;
import com.softserve.academy.event.dto.ItemDTO;
import com.softserve.academy.event.service.db.ContactService;
import com.softserve.academy.event.service.mapper.ContactMapper;
import com.softserve.academy.event.util.Page;
import com.softserve.academy.event.util.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    @PostMapping
    public ResponseEntity<ItemDTO<Long>> createContact(@RequestBody ContactDTO contactDTO) {
        return ResponseEntity.ok(
                mapper.toItemDTO(
                        service.save(contactDTO)
                )
        );
    }

    @PutMapping
    public ResponseEntity<HttpStatus> updateContact(@RequestBody ContactDTO contactDTO) {
        service.update(contactDTO);
        return ResponseEntity.ok(HttpStatus.OK);
    }

    @DeleteMapping
    public ResponseEntity<HttpStatus> deleteContact(@RequestParam Long id) {
        service.delete(id);
        return ResponseEntity.ok(HttpStatus.OK);
    }

}
