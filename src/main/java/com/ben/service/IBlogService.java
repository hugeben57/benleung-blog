package com.ben.service;

import com.ben.model.dto.BlogDTO;
import com.ben.model.entity.Blog;
import com.ben.model.entity.Result;
import com.ben.model.vo.BlogListVO;
import com.ben.model.vo.BlogVO;

import java.util.List;
import java.util.Map;

public interface IBlogService {

    <T> Result<T> addBlog(BlogDTO blogDTO);

    <T> Result<T> deleteBlog(Long id);

    <T> Result<T> updateBlog(Long id,BlogDTO blogDTO);

    Result<BlogVO> getBlogById(Long id);

//    Result<List<BlogListVO>> getBlogList();

    Result<List<BlogListVO>> getAdminBlogList();

    <T> Result<T> published(Long id);

    Result<List<Map<String,Long>>> getBlogTypes();

    Result<List<BlogVO>> getBlogByTypes(String type);

    Result<List<BlogVO>> getBlogPage(Long currentPage,Long pageSize);

    Long getLatestBlogId();
}
