package com.ben.model.vo;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BlogVO {

    private Long id;

    private String title;

    private String content;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;

    private String description;

    private Integer published;

    private String type;

}
