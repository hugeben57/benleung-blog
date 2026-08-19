package com.ben.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ben.mapper.MusicMapper;
import com.ben.model.entity.Music;
import com.ben.model.entity.Result;
import com.ben.service.IMinioService;
import com.ben.service.IMusicService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
public class MusicServiceImpl extends ServiceImpl<MusicMapper, Music> implements IMusicService {

    @Autowired
    private IMinioService minioService;

    @Override
    public Result<Void> addMusic(MultipartFile file, String musicName) {
        String url=minioService.uploadFile(file, "music");
        Music music=new Music();
        music.setMusicName(musicName);
        music.setUrl(url);
        if (save(music)){
            return Result.success();
        }
        return Result.fail("上传失败");
    }

    @Override
    public Result<Music> getMusic() {
        List<Music> musicList=list();
        if (musicList==null || musicList.isEmpty()){
            return Result.fail("歌曲列表为空");
        }
        Music music = musicList.get(0);
        return Result.success(music);
    }

    @Override
    public Result<Void> updateMusic(MultipartFile file, String musicName) {
        String url=minioService.uploadFile(file, "music");
        Music music=new Music();
        music.setMusicName(musicName);
        music.setUrl(url);
        music.setId(1L);
        if (updateById(music)){
            return Result.success();
        }
        return Result.fail("更新失败");
    }
}
