package com.ben.service;


import com.ben.model.entity.Music;
import com.ben.model.entity.Result;
import org.springframework.web.multipart.MultipartFile;

public interface IMusicService{

    Result<Void> addMusic(MultipartFile file, String musicName);

    Result<Music> getMusic();

    Result<Void> updateMusic(MultipartFile file, String musicName);

}
