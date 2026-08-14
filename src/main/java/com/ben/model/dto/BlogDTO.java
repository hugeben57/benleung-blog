package com.ben.model.dto;

import com.baomidou.mybatisplus.annotation.TableField;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BlogDTO {

    private String title;

    private String content;

    private String description;

    private Integer published;

    private String type;

    private String tag;

}
