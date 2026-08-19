package com.ben.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@TableName("music")
public class Music {
    @TableId(type = IdType.AUTO)
    private Long id;

    @TableField("url")
    private String url;

    @TableField("music_name")
    private String musicName;
}
