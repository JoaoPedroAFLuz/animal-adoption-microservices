package br.com.joaopedroafluz.userservice.domain.service;

import org.springframework.web.multipart.MultipartFile;

public interface ImageStorageService {

    String upload(MultipartFile file);

    void delete(String imageUrl);

}
