package com.ben.model.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class Result<T> {

    private Integer code;
    private String message;
    private T data;

    public static <T> Result<T> success(){
        Result<T> res=new Result<>();
        res.code=200;
        res.message="success";
        return res;
    }

    public static <T> Result<T> success(T data){
        Result<T> res=new Result<>();
        res.code=200;
        res.message="success";
        res.data=data;
        return res;
    }

    public static <T> Result<T> fail(){
        Result<T> res=new Result<>();
        res.code=500;
        res.message="fail";
        return res;
    }
    public static <T> Result<T> fail(String message){
        Result<T> res=new Result<>();
        res.code=500;
        res.message=message;
        return res;
    }
    public static <T> Result<T> fail(Integer code,String message){
        Result<T> res=new Result<>();
        res.code=code;
        res.message=message;
        return res;
    }




}
