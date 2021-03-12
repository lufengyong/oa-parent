package com.openoa.admin.ultil;

import com.fasterxml.jackson.databind.json.JsonMapper;

public class R {
    public Integer code;
    public String message;
    public Object data;

    public R(Integer code, String message, Object data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }

    public static R ok(Object obj) {
        return new R(1,"success",obj);
    }

    public static R error(String message) {
        return new R(0,message,null);
    }
}
