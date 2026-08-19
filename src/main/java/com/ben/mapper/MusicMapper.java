package com.ben.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ben.model.entity.Music;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface MusicMapper extends BaseMapper<Music> {
}
