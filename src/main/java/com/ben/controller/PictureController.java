package com.ben.controller;

import com.ben.model.vo.PictureVO;
import com.ben.model.entity.Result;
import com.ben.service.impl.PictureServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/Picture")
public class PictureController {

    @Autowired
    private PictureServiceImpl pictureService;

    @PostMapping("/uploadPicture")
    public Result<Void> uploadPicture(MultipartFile file, String pictureName) {
        return pictureService.uploadPicture(file, pictureName);
    }

    @GetMapping("/getPictures")
    public Result<List<PictureVO>> getPictures() {
        return pictureService.getPictures();
    }

    @GetMapping("/getPictureById/{id}")
    public Result<PictureVO> getPictureById(@PathVariable Long id) {
        return pictureService.getPictureById(id);
    }

    @DeleteMapping("/deletePictureById/{id}")
    public Result<Void> deletePictureById(@PathVariable Long id) {
        return pictureService.deletePictureById(id);
    }

    @PostMapping("/updatePicture/{id}")
    public Result<Void> updatePicture(@PathVariable Long id, String pictureName) {
        return pictureService.updatePicture(id, pictureName);
    }

    @PostMapping("/setCoverPicture/{id}")
    public Result<PictureVO> setCoverPicture(@PathVariable Long id) {
        return pictureService.setCoverPicture(id);
    }
}
