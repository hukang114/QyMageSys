package com.qymage.sys.common.callback;

import java.io.Serializable;

public class SimpleResponse implements Serializable {

    private static final long serialVersionUID = -1477609349345966116L;

    public int code;
    public String message;

    public Result toLzyResponse() {
        Result lzyResponse = new Result();
        lzyResponse.code = code;
        lzyResponse.message = message;
        return lzyResponse;
    }
}