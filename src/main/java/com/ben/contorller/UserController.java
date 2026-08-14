package com.ben.contorller;

import com.ben.model.dto.UserDTO;
import com.ben.model.entity.Result;
import com.ben.service.impl.UserServiceImpl;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("user")
public class UserController {

    @Autowired
    private UserServiceImpl userService;

    @PostMapping("signUp")
    public <T> Result<T> signUp(@Valid @RequestBody UserDTO userDTO) {
        return userService.signUp(userDTO);
    }


    @PostMapping("signIn")
    public  Result<Map<String,Object>> signIn(@Valid @RequestBody UserDTO userDTO){
        return userService.signIn(userDTO);
    }


}
