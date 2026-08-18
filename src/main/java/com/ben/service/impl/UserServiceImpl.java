package com.ben.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ben.mapper.UserMapper;
import com.ben.model.dto.UserDTO;
import com.ben.model.entity.Result;
import com.ben.model.entity.User;
import com.ben.service.IUserService;
import com.ben.utils.JwtUtils;
import com.ben.utils.ThreadLocalUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements IUserService {

    @Override
    public <T> Result<T> signUp(UserDTO userDTO) {
        String userName=userDTO.getUserName();
        LambdaQueryWrapper<User> wrapper=new LambdaQueryWrapper<>();
        wrapper.eq(User::getUserName,userName);
        if(exists(wrapper)){
            return Result.fail("用户名已使用");
        }
        User user=new User();
        BeanUtils.copyProperties(userDTO,user);
        user.setRole(0);
        if (save(user)){
            return Result.success();
        }
        return Result.fail("注册失败");
    }

    @Override
    public Result<Map<String,Object>> signIn(UserDTO userDTO) {
        String userName=userDTO.getUserName();
        String password=userDTO.getPassword();
        LambdaQueryWrapper<User> wrapper=new LambdaQueryWrapper<>();
        wrapper.eq(User::getUserName,userName);
        User userRecord=getOne(wrapper);
        if (userRecord==null){
            return Result.fail("此用户不存在");
        }
        if (!userRecord.getPassword().equals(password)){
            return Result.fail("密码错误");
        }
        Map<String,Object> claim=new HashMap<>();
        claim.put("userId",userRecord.getId());
        claim.put("role",userRecord.getRole());
        String token= JwtUtils.getToken(claim);
        Map<String,Object> data=new HashMap<>();
        data.put("token",token);
        data.put("role",userRecord.getRole());
        return Result.success(data);
    }

    @Override
    public Result<Map<String, Object>> getUserInfo() {
        Map<String, Object> claim = ThreadLocalUtil.get();
        Long userId = ((Number) claim.get("userId")).longValue();
        User user = getById(userId);
        if (user == null) {
            return Result.fail("用户不存在");
        }
        Map<String, Object> data = new HashMap<>();
        data.put("userId", user.getId());
        data.put("userName", user.getUserName());
        data.put("role", user.getRole());
        return Result.success(data);
    }
}
