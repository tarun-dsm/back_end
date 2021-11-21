package toyproject.syxxn.back_end.service.util;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.PutObjectRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import toyproject.syxxn.back_end.exception.FileIsEmptyException;
import toyproject.syxxn.back_end.exception.FileSaveFailedException;
import toyproject.syxxn.back_end.exception.InvalidFileExtensionException;

import java.io.*;
import java.util.UUID;

@RequiredArgsConstructor
@Component
public class S3Util {

    private final AmazonS3 amazonS3Client;

    @Value("${aws.s3.bucket}")
    private String bucketName;

    @Value("${aws.s3.base-image-url}")
    private String baseImageUrl;

    public void delete(String objectName) {
        amazonS3Client.deleteObject(bucketName, objectName);
    }

    public String uploadImage(MultipartFile file) throws IOException {
        String extension = verificationFile(file);
        String filePath;
        try{
            filePath = saveImage(file, extension);
        } catch (IOException e) {
            throw new FileSaveFailedException();
        }
        return filePath;
    }

    /*public String generateObjectUrl(String filePath){ 를 쓰려고 했는데 값이 요상하게 와서 일단 지워둠.
            Date expiration = new Date();
            expiration.setTime(expiration.getTime() + EXP_TIME);

            GeneratePresignedUrlRequest generatePresignedUrlRequest =
                    new GeneratePresignedUrlRequest(bucketName, filePath)
                            .withMethod(HttpMethod.GET)
                            .withExpiration(expiration);

            URL url = amazonS3Client.generatePresignedUrl(generatePresignedUrlRequest);

            return url.toExternalForm();
    }*/

    private String verificationFile(MultipartFile file) {
        if(file == null || file.isEmpty() || file.getOriginalFilename() == null)
            throw new FileIsEmptyException();

        String extension = file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf("."));
        if (!(extension.contains(".JPG") || extension.contains(".jpg") || extension.contains(".jpeg") || extension.contains(".JPEG") || extension.contains(".png") || extension.contains(".PNG"))) {
            throw new InvalidFileExtensionException();
        }

        return extension;
    }

    private String saveImage(MultipartFile file, String extension) throws IOException {
        String filePath = baseImageUrl + UUID.randomUUID() + extension;

        amazonS3Client.putObject(new PutObjectRequest(bucketName, filePath, file.getInputStream(), null)
                .withCannedAcl(CannedAccessControlList.PublicRead));
        return amazonS3Client.getUrl(bucketName, filePath).toString(); // key는 버킷/파일명인듯
    }

}
