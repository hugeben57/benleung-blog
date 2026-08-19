package com.ben.controller;
import com.ben.model.entity.Music;
import com.ben.model.entity.Result;
import com.ben.service.IMusicService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/music")
public class MusicController {

    @Autowired
    private IMusicService musicService;

    @RequestMapping("/add")
    public Result<Void> addMusic(MultipartFile file, String musicName) {
        return musicService.addMusic(file, musicName);
    }

    @RequestMapping("/get")
    public Result<Music> getMusic() {
        return musicService.getMusic();
    }

    @RequestMapping("/update")
    public Result<Void> updateMusic(MultipartFile file, String musicName) {
        return musicService.updateMusic(file, musicName);
    }
}
