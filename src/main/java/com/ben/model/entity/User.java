package com.ben.model.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@TableName("`user`")
public class User {


    private Long id;

    @TableField("username")
    private String userName;

    private String password;

    private Integer role;


}
