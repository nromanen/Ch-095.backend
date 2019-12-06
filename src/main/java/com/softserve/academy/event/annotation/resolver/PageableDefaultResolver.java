package com.softserve.academy.event.annotation.resolver;

import com.softserve.academy.event.annotation.PageableDefault;
import com.softserve.academy.event.util.Pageable;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.ModelAndViewContainer;

public class PageableDefaultResolver implements CustomHandlerMethodArgumentResolver<Pageable> {

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.getParameterAnnotation(PageableDefault.class) != null;
    }

    @Override
    public Pageable resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer,
                                  NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
        PageableDefault annotation = parameter.getParameterAnnotation(PageableDefault.class);
        return new Pageable(annotation.page(), 0, annotation.size());
    }

}
