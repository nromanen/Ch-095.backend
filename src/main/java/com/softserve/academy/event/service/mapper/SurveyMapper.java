package com.softserve.academy.event.service.mapper;

import com.softserve.academy.event.dto.SurveyDTO;
import com.softserve.academy.event.entity.Survey;
import com.softserve.academy.event.util.Page;
import com.softserve.academy.event.util.SurveyPage;
import org.mapstruct.Mapper;
import org.springframework.stereotype.Service;

@Mapper(componentModel = "spring")
@Service
public interface SurveyMapper {

    SurveyDTO toDTO(Survey survey);

    SurveyPage<SurveyDTO> pageToDTO(Page<Survey> list);

}
