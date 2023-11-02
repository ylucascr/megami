package com.esufam.megami.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class Response {
    String status;
    String message;
    Object data;

    public static Response success(Object data) {
        return new Response("success", null, data);
    }

    public static Response fail(Object data) {
        return new Response("fail", null, data);
    }

    public static Response error(String message, Object data) {
        return new Response("error", message, data);
    }
}