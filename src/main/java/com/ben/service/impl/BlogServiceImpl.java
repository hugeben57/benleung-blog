package com.ben.service.impl;

import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ben.mapper.BlogMapper;
import com.ben.model.dto.BlogDTO;
import com.ben.model.entity.Blog;
import com.ben.model.entity.Result;
import com.ben.model.vo.BlogListVO;
import com.ben.model.vo.BlogVO;
import com.ben.service.IBlogService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class BlogServiceImpl extends ServiceImpl<BlogMapper, Blog> implements IBlogService {

    @Override
    public <T> Result<T> addBlog(BlogDTO blogDTO) {
        Blog blog =new Blog();
        BeanUtils.copyProperties(blogDTO,blog);
        blog.setCreateTime(LocalDateTime.now());
        blog.setUpdateTime(LocalDateTime.now());
        if(save(blog)){
            return Result.success();
        }
        return Result.fail();
    }

    @Override
    public <T> Result<T> deleteBlog(Long id) {
        if(removeById(id)){
            return Result.success();
        }
        return Result.fail();
    }

    @Override
    public <T> Result<T> updateBlog(Long id, BlogDTO blogDTO) {
        Blog blog=new Blog();
        BeanUtils.copyProperties(blogDTO,blog);
        blog.setId(id);
        blog.setUpdateTime(LocalDateTime.now());
        if(updateById(blog)){
            return Result.success();
        }
        return Result.fail();
    }

    @Override
    public Result<BlogVO> getBlogById(Long id) {
        Blog blog=getById(id);
        if (blog==null){
            return Result.fail("文章不存在");
        }
        BlogVO blogVO=new BlogVO();
        BeanUtils.copyProperties(blog,blogVO);
        return Result.success(blogVO);
    }

    @Override
    public Result<List<BlogListVO>> getBlogList() {
        List<Blog> blogList=list();
        if(blogList==null || blogList.isEmpty()){
            return Result.fail("没有找到文章");
        }
        List<BlogListVO> blogVOList=blogList.stream().map(blog->{
            BlogListVO blogListVO=new BlogListVO();
            BeanUtils.copyProperties(blog,blogListVO);
            return blogListVO;
        }).toList();
        return Result.success(blogVOList);
    }
}
