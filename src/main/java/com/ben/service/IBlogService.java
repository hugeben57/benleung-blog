package com.ben.service;

import com.ben.model.dto.BlogDTO;
import com.ben.model.entity.Blog;
import com.ben.model.entity.Result;
import com.ben.model.vo.BlogListVO;
import com.ben.model.vo.BlogVO;

import java.util.List;

public interface IBlogService {

    <T> Result<T> addBlog(BlogDTO blogDTO);

    <T> Result<T> deleteBlog(Long id);

    <T> Result<T> updateBlog(Long id,BlogDTO blogDTO);

    Result<BlogVO> getBlogById(Long id);

    Result<List<BlogListVO>> getBlogList();

}
