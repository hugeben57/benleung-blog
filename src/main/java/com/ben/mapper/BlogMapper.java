package com.ben.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ben.model.entity.Blog;
import com.ben.model.vo.BlogVO;
import org.apache.ibatis.annotations.Mapper;
import java.util.List;
import java.util.Map;

@Mapper
public interface BlogMapper extends BaseMapper<Blog> {

    List<Map<String,Long>> getBlogTypes();

    Long getLatestBlogId();

}
