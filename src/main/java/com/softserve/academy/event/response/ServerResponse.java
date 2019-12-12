package com.softserve.academy.event.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.http.HttpStatus;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ServerResponse<T> {

    private T result;
    private HttpStatus status;
    private String message;

    private ServerResponse(T result, HttpStatus status) {
        this.result = result;
        this.status = status;
    }

    public static <T> ServerResponse<T> success(T result) {
        return from(result, HttpStatus.OK);
    }

    public static <T> ServerResponse<T> success(T result, String message) {
        return from(result, HttpStatus.OK, message);
    }

    public static <T> ServerResponse<T> from(T result, HttpStatus status) {
        return new ServerResponse<T>(result, status);
    }

    public static <T> ServerResponse<T> from(T result, HttpStatus status, String message) {
        return new ServerResponse<T>(result, status, message);
    }

}
