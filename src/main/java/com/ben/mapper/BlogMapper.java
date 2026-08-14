package com.ben.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ben.model.dto.BlogDTO;
import com.ben.model.entity.Blog;
import com.ben.model.entity.Result;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface BlogMapper extends BaseMapper<Blog> {
}
