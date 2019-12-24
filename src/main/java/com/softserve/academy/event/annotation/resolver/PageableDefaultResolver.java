package com.softserve.academy.event.annotation.resolver;

import com.softserve.academy.event.annotation.PageableDefault;
import com.softserve.academy.event.util.Pageable;
import com.softserve.academy.event.util.Sort;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import java.util.Objects;

public class PageableDefaultResolver implements HandlerMethodArgumentResolver {

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.getParameterAnnotation(PageableDefault.class) != null;
    }

    @Override
    public Pageable resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer,
                                    NativeWebRequest webRequest, WebDataBinderFactory binderFactory) {
        PageableDefault annotation = parameter.getParameterAnnotation(PageableDefault.class);
        String size = webRequest.getParameter(annotation.params()[0]);
        String page = webRequest.getParameter(annotation.params()[1]);
        String sort = webRequest.getParameter(annotation.params()[2]);
        String direction = webRequest.getParameter(annotation.params()[3]);
        return new Pageable(
                Objects.isNull(size) ? annotation.size() : Integer.parseInt(size),
                Objects.isNull(page) ? annotation.page() : Integer.parseInt(page),
                0,
                Sort.from(Objects.isNull(direction)  ? annotation.direction() : Sort.Direction.valueOf(direction),
                        Objects.isNull(sort) ? annotation.sort() : sort)
        );
    }

}
