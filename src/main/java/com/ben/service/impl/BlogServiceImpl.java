package com.ben.service.impl;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ben.mapper.BlogMapper;
import com.ben.model.dto.BlogDTO;
import com.ben.model.entity.Blog;
import com.ben.model.entity.Result;
import com.ben.model.vo.BlogListVO;
import com.ben.model.vo.BlogVO;
import com.ben.service.IBlogService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Service
public class BlogServiceImpl extends ServiceImpl<BlogMapper, Blog> implements IBlogService {

    @Autowired
    BlogMapper blogMapper;

    @Override
    @CacheEvict(cacheNames = "blog", key = "'adminBlogList'")
    public <T> Result<T> addBlog(BlogDTO blogDTO) {
        Blog blog =new Blog();
        BeanUtils.copyProperties(blogDTO,blog);
        blog.setCreateTime(LocalDateTime.now());
        blog.setUpdateTime(LocalDateTime.now());
        blog.setPublished(0);
        if(save(blog)){
            return Result.success();
        }
        return Result.fail();
    }

    @Override
    @Caching(evict = {
            @CacheEvict(cacheNames = "blogPage",allEntries = true),
            @CacheEvict(cacheNames = "blog", key = "'adminBlogList'"),
            @CacheEvict(cacheNames = "blog", allEntries = true)
    })
    public <T> Result<T> deleteBlog(Long id) {
        if(removeById(id)){
            return Result.success();
        }
        return Result.fail();
    }

    @Override
    @Caching(evict = {
            @CacheEvict(cacheNames = "blogPage",allEntries = true),
            @CacheEvict(cacheNames = "blog", key = "'adminBlogList'"),
            @CacheEvict(cacheNames = "blog", allEntries = true)

    })
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

//    @Override
//    @Cacheable(cacheNames = "blog",key = "'blogList'",unless = "#result.data==null")
//    public Result<List<BlogListVO>> getBlogList() {
//        log.info("走数据库查询");
//        LambdaQueryWrapper<Blog> wrapper=new LambdaQueryWrapper<>();
//        wrapper.eq(Blog::getPublished,1);
//        List<Blog> blogList=list(wrapper);
//        if(blogList==null || blogList.isEmpty()){
//            return Result.fail("没有找到文章");
//        }
//        List<BlogListVO> blogVOList=blogList.stream().map(blog->{
//            BlogListVO blogListVO=new BlogListVO();
//            BeanUtils.copyProperties(blog,blogListVO);
//            return blogListVO;
//        }).toList();
//        return Result.success(blogVOList);
//    }


    @Override
    @Cacheable(cacheNames = "blog",key = "'adminBlogList'",unless = "#result.data==null")
    public Result<List<BlogListVO>> getAdminBlogList() {
        log.info("走数据库");
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

    @Override
    @Caching(evict = {
            @CacheEvict(cacheNames = "blogPage",allEntries = true),
            @CacheEvict(cacheNames = "blog", key = "'adminBlogList'"),
            @CacheEvict(cacheNames = "blog", allEntries = true)

    })
    public <T> Result<T> published(Long id) {
        Blog blog=getById(id);
        blog.setPublished(1);
        blog.setPublishedTime(LocalDateTime.now());
        if(updateById(blog)){
            return Result.success();
        }
        return Result.fail();
    }

    @Override
    public Result<List<Map<String, Long>>> getBlogTypes() {
        return Result.success(blogMapper.getBlogTypes());
    }

    @Override
    @Cacheable(cacheNames = "blog", key = "#type")
    public Result<List<BlogVO>> getBlogByTypes(String type) {
        LambdaQueryWrapper<Blog> wrapper=new LambdaQueryWrapper<>();
        wrapper.eq(Blog::getType,type).eq(Blog::getPublished,1);
        List<Blog> blogList=list(wrapper);
        if (blogList==null || blogList.isEmpty()){
            return Result.fail("文章列表为空");
        }
        List<BlogVO> blogVOList=blogList.stream().map(blog->{
            BlogVO blogVO=new BlogVO();
            BeanUtils.copyProperties(blog,blogVO);
            return blogVO;
        }).toList();
        return Result.success(blogVOList);
    }

    @Override
    @Cacheable(cacheNames = "blogPage", key = "#currentPage + '_' + #pageSize")
    public Result<List<BlogVO>> getBlogPage(Long currentPage,Long pageSize){
        log.info("数据库分页");
        //参数对象
        Page<Blog> pages=new Page<>(currentPage,pageSize);
        //查询条件
        LambdaQueryWrapper<Blog> wrapper=new LambdaQueryWrapper<>();
        wrapper.eq(Blog::getPublished,1);
        Page<Blog> blogPage = page(pages,wrapper);
        if(blogPage==null || blogPage.getRecords().isEmpty()){
            return Result.fail("没有找到文章");
        }
        List<Blog> blogList = blogPage.getRecords();
        List<BlogVO> blogVOList=blogList.stream().map(blog->{
            BlogVO blogVO=new BlogVO();
            BeanUtils.copyProperties(blog,blogVO);
            return blogVO;
        }).toList();
        return Result.success(blogVOList);
    }

}
