package com.baihe.http.exception;

import androidx.annotation.Nullable;

import java.util.Objects;

import okhttp3.Response;

public class HttpException extends RuntimeException{
    private static String getMessage(Response response){
        Objects.requireNonNull(response,"response == null");
        return "HTTP "+ response.code() + " " + response.message();
    }

    private final int code;
    private final String message;
    private final transient Response response;

    public HttpException(Response response){
        super(getMessage(response));
        this.code = response.code();
        this.message = response.message();
        this.response = response;
    }


    public int code() {
        return code;
    }


    public String message() {
        return message;
    }


    public @Nullable Response response() {
        return response;
    }
}
