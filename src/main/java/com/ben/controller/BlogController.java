package com.ben.controller;

import com.ben.model.dto.BlogDTO;
import com.ben.model.entity.Result;
import com.ben.model.vo.BlogListVO;
import com.ben.model.vo.BlogVO;
import com.ben.service.impl.BlogServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/blog")
public class BlogController {

    @Autowired
    BlogServiceImpl blogService;

    @PostMapping("/addBlog")
    public <T> Result<T> addBlog(@RequestBody BlogDTO blogDTO){
        return blogService.addBlog(blogDTO);
    }

    @DeleteMapping("/deleteBlog/{id}")
    public <T> Result<T> deleteBlog(@PathVariable Long id){
        return blogService.deleteBlog(id);
    }

    @PostMapping("/updateBlog/{id}")
    public <T> Result<T> updateBlog(@PathVariable Long id, @RequestBody BlogDTO blogDTO){
        return blogService.updateBlog(id,blogDTO);
    }

    @GetMapping("/getBlogById/{id}")
    public Result<BlogVO> getBlogById(@PathVariable Long id){
        return blogService.getBlogById(id);
    }

    @GetMapping("/getBlogList")
    public Result<List<BlogListVO>> getBlogList(){
        return blogService.getBlogList();
    }

    @GetMapping("/getAdminBlogList")
    public Result<List<BlogListVO>> getAdminBlogList(){
        return blogService.getAdminBlogList();
    }

    @PostMapping("published/{id}")
    public <T> Result<T> published(@PathVariable Long id){
        return blogService.published(id);
    }

    @GetMapping("/getBlogTypes")
    public Result<List<Map<String, Long>>> getBlogTypes(){
        return blogService.getBlogTypes();
    }

    @GetMapping("getBlogByTypes")
    public Result<List<BlogVO>> getBlogByTypes(@RequestParam String type){
        return blogService.getBlogByTypes(type);
    }

    @GetMapping("/getBlogPages")
    public Result<List<BlogVO>> getBlogPages(@RequestParam(defaultValue = "1") Long currentPage,@RequestParam(defaultValue = "6") Long pageSize){
        return blogService.getBlogPage(currentPage,pageSize);
    }



}
