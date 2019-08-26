package com.qymage.sys.common.callback;

import java.io.Serializable;

public class Result<T> implements Serializable {

    private static final long serialVersionUID = 5213230387175987834L;

    public int code;
    public String message;
    public T data;
}