package com.ben.service;

import com.ben.model.dto.UserDTO;
import com.ben.model.entity.Result;
import org.apache.ibatis.annotations.Mapper;

import java.util.Map;

@Mapper
public interface IUserService {

    <T> Result<T> signUp(UserDTO userDTO);

    Result<Map<String, Object>> signIn(UserDTO userDTO);


}
