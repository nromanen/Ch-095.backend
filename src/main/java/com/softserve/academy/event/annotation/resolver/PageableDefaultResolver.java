package com.softserve.academy.event.annotation.resolver;

import com.softserve.academy.event.annotation.PageableDefault;
import com.softserve.academy.event.util.Pageable;
import com.softserve.academy.event.util.Sort;
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
        Integer size = Integer.valueOf(webRequest.getParameter(annotation.params()[0]));
        Integer page = Integer.valueOf(webRequest.getParameter(annotation.params()[1]));
        String[] sort = webRequest.getParameterValues(annotation.params()[2]);
        Sort.Direction direction = Sort.Direction.valueOf(webRequest.getParameter(annotation.params()[3]));
        return new Pageable(
                page == null ? annotation.page() : page,
                0,
                size == null ? annotation.size() : size,
                Sort.from(direction == null ? annotation.direction() : direction,
                        sort == null ? annotation.sort() : sort)
        );
    }

}
