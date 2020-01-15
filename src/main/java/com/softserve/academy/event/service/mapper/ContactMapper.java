package com.softserve.academy.event.service.mapper;

import com.softserve.academy.event.dto.ContactDTO;
import com.softserve.academy.event.dto.ItemDTO;
import com.softserve.academy.event.entity.Contact;
import com.softserve.academy.event.util.Page;
import org.mapstruct.Mapper;
import org.springframework.stereotype.Service;

@Mapper(componentModel = "spring")
@Service
public interface ContactMapper {

    ItemDTO<Long> toItemDTO(Long id);
    ContactDTO toDTO(Contact contact);
    Page<ContactDTO> toPageDTO(Page<Contact> contacts);

}
