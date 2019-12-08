package com.softserve.academy.event.annotation.resolver;

import com.softserve.academy.event.annotation.PageableDefault;
import com.softserve.academy.event.util.Pageable;
import com.softserve.academy.event.util.Sort;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

public class PageableDefaultResolver implements HandlerMethodArgumentResolver {

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.getParameterAnnotation(PageableDefault.class) != null;
    }

    @Override
    public Pageable resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer,
                                    NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
        PageableDefault annotation = parameter.getParameterAnnotation(PageableDefault.class);
        String size = webRequest.getParameter(annotation.params()[0]);
        String page = webRequest.getParameter(annotation.params()[1]);
        String[] sort = webRequest.getParameterValues(annotation.params()[2]);
        String direction = webRequest.getParameter(annotation.params()[3]);
        return new Pageable(
                size == null ? annotation.size() : Integer.parseInt(size),
                page == null ? annotation.page() : Integer.parseInt(page),
                0,
                Sort.from(direction == null ? annotation.direction() : Sort.Direction.valueOf(direction),
                        sort == null || sort.length == 0 ? annotation.sort() : sort)
        );
    }

}
