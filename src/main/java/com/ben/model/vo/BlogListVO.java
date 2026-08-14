package com.ben.model.vo;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class BlogListVO {

    private Long id;

    private String title;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;

    private String description;

    private Integer published;

    private String type;

}
