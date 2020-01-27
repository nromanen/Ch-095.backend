package com.softserve.academy.event.service.mapper;

import com.softserve.academy.event.dto.ContactDTO;
import com.softserve.academy.event.dto.ItemDTO;
import com.softserve.academy.event.entity.Contact;
import com.softserve.academy.event.util.Page;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.stereotype.Service;

import java.util.List;

@Mapper(componentModel = "spring")
@Service
public interface ContactMapper {

    @Mapping(target = "item", expression = "java(item)")
    ItemDTO<Long> toItemDTO(Long item);
    @Mapping(target = "item", expression = "java(item)")
    ItemDTO<String> toItemDTO(String item);

    ContactDTO toDTO(Contact contact);
    List<ContactDTO> toListDTO(List<Contact> contact);
    Page<ContactDTO> toPageDTO(Page<Contact> contacts);

}
