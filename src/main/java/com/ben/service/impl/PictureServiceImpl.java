package com.ben.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ben.mapper.PictureMapper;
import com.ben.model.entity.Picture;
import com.ben.model.entity.Result;
import com.ben.model.vo.PictureVO;
import com.ben.service.IMinioService;
import com.ben.service.IPictureService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
public class PictureServiceImpl extends ServiceImpl<PictureMapper, Picture> implements IPictureService {

    @Autowired
    IMinioService minioService;

    @Override
    public Result<Void> uploadPicture(MultipartFile file,String pictureName) {
        String url=minioService.uploadFile(file, "picture");
        Picture picture=new Picture();
        picture.setUrl(url);
        picture.setPictureName(pictureName);
        if (save(picture)){
            return Result.success();
        }
        return Result.fail("上传失败");
    }

    @Override
    public Result<PictureVO> getPictureById(Long id) {
        Picture picture = getById(id);
        if (picture != null) {
            PictureVO pictureVO = new PictureVO();
            BeanUtils.copyProperties(picture, pictureVO);
            return Result.success(pictureVO);
        }
        return Result.fail("没有找到图片");
    }

    @Override
    public Result<List<PictureVO>> getPictures() {
        List<Picture> pictures = list();
        if (pictures == null || pictures.isEmpty()){
            return Result.success(List.of());
        }
        List<PictureVO> pictureVOList=pictures.stream().map(
                picture -> {
                    PictureVO pictureVO=new PictureVO();
                    BeanUtils.copyProperties(picture,pictureVO);
                    return pictureVO;
                }
        ).toList();
        return Result.success(pictureVOList);

    }

    @Override
    public Result<Void> deletePictureById(Long id) {
        Picture picture = getById(id);
        if (picture == null) {
            return Result.fail("没有找到图片");
        }
        minioService.deleteFile(picture.getUrl());
        removeById(id);
        return Result.success();
    }
}
