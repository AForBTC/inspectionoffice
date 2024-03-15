package com.ws.inspectionoffice.utils;

public class JsonResponse {

    private int code;
    private String message;
    private Object data;

    public static final JsonResponse wrapper() { return new JsonResponse(); }

    public JsonResponse() {}

    public JsonResponse(int code, String message, Object data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }

    public JsonResponse code(int code) {
        this.code = code;
        return this;
    }

    public JsonResponse message(String msg) {
        this.message = msg;
        return this;
    }

    public JsonResponse data(Object data) {
        this.data = data;
        return this;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

}
