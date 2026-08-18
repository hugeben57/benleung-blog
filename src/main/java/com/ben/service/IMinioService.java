package com.ben.service;

import org.springframework.web.multipart.MultipartFile;

public interface IMinioService {

    /**
     * 上传文件到 MinIO
     * @param file   要上传的文件
     * @param folder 存储文件的目录
     * @return 文件访问 URL
     */
    String uploadFile(MultipartFile file, String folder);

    /**
     * 删除 MinIO 文件
     * @param fileUrl 文件 URL
     */
    void deleteFile(String fileUrl);


}
