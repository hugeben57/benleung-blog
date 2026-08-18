package com.ben.service;

import com.ben.model.entity.Picture;
import com.ben.model.vo.PictureVO;
import org.springframework.web.multipart.MultipartFile;
import com.ben.model.entity.Result;

import java.util.List;

public interface IPictureService {

    Result<Void> uploadPicture(MultipartFile file, String pictureName);

    Result<List<PictureVO>> getPictures();

    Result<PictureVO> getPictureById(Long id);

    Result<Void> deletePictureById(Long id);

    Result<Void> updatePicture(Long id, String pictureName);

}
