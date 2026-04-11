package br.com.joaopedroafluz.userservice.infrastructure;

import br.com.joaopedroafluz.userservice.config.MinioProperties;
import br.com.joaopedroafluz.userservice.domain.service.ImageStorageService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.io.IOException;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class S3ImageStorageService implements ImageStorageService {

    private final S3Client s3Client;
    private final MinioProperties minioProperties;

    @Override
    public String upload(MultipartFile file) {
        final var extension = getExtension(file.getOriginalFilename());
        final var key = UUID.randomUUID() + extension;

        try {
            final var request = PutObjectRequest.builder()
                                                .bucket(minioProperties.getBucket())
                                                .key(key)
                                                .contentType(file.getContentType())
                                                .build();

            s3Client.putObject(request, RequestBody.fromBytes(file.getBytes()));
        } catch (IOException e) {
            throw new RuntimeException("Failed to upload image", e);
        }

        return minioProperties.getEndpoint() + "/" + minioProperties.getBucket() + "/" + key;
    }

    @Override
    public void delete(String imageUrl) {
        final var key = imageUrl.substring(imageUrl.lastIndexOf("/") + 1);

        final var request = DeleteObjectRequest.builder()
                                               .bucket(minioProperties.getBucket())
                                               .key(key)
                                               .build();

        s3Client.deleteObject(request);
    }

    private String getExtension(String filename) {
        if (filename != null && filename.contains(".")) {
            return filename.substring(filename.lastIndexOf("."));
        }

        return ".jpg";
    }

}
