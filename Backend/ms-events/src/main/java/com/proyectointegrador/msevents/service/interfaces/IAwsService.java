package com.proyectointegrador.msevents.service.interfaces;

import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.List;

public interface IAwsService {
    String uploadFile(MultipartFile file) throws Exception;

    List<String> getObjectsFromS3();

    InputStream downloadFile(String key);

    String generateImageUrl(String fileName);
}
