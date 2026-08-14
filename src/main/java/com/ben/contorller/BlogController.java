package com.ben.contorller;

import com.ben.model.dto.BlogDTO;
import com.ben.model.entity.Result;
import com.ben.model.vo.BlogListVO;
import com.ben.model.vo.BlogVO;
import com.ben.service.impl.BlogServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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


}
