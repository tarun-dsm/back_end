package toyproject.syxxn.back_end.service.util;

import com.amazonaws.HttpMethod;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.GeneratePresignedUrlRequest;
import com.amazonaws.services.s3.model.PutObjectRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import toyproject.syxxn.back_end.exception.FileIsEmptyException;
import toyproject.syxxn.back_end.exception.FileSaveFailedException;
import toyproject.syxxn.back_end.exception.InvalidFileExtensionException;

import java.io.*;
import java.net.URL;
import java.util.Date;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

@RequiredArgsConstructor
@Component
public class S3Util {

    private final AmazonS3Client amazonS3Client;

    @Value("${aws.s3.bucket}")
    private String bucketName;

    @Value("${aws.s3.base-image-url}")
    private String baseImageUrl;

    private static final int EXP_TIME = 1000 * 60 * 2;

    public void delete(String objectName) {
        amazonS3Client.deleteObject(bucketName, objectName);
    }

    public String uploadImage(MultipartFile file) {
        String extension = verificationFile(file);
        String filePath;
        try{
            File uploadFile = convertMultipartFileToFile(file)
                    .orElseThrow(FileSaveFailedException::new);

            filePath = saveImage(uploadFile, extension);
        } catch (IOException e) {
            throw new FileSaveFailedException();
        }

        return filePath;
    }

    public String generateObjectUrl(String filePath) {
        Date expiration = new Date();
        expiration.setTime(expiration.getTime() + EXP_TIME);

        GeneratePresignedUrlRequest generatePresignedUrlRequest =
                new GeneratePresignedUrlRequest(bucketName, filePath)
                        .withMethod(HttpMethod.GET)
                        .withExpiration(expiration);

        URL url = amazonS3Client.generatePresignedUrl(generatePresignedUrlRequest);

        return url.toString();
    }

    private String verificationFile(MultipartFile file) {
        if(file == null || file.isEmpty() || file.getOriginalFilename() == null)
            throw new FileIsEmptyException();

        String extension = file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf("."));
        if (!(extension.contains(".JPG") || extension.contains(".jpg") || extension.contains(".jpeg") || extension.contains(".JPEG") || extension.contains(".png") || extension.contains(".PNG"))) {
            throw new InvalidFileExtensionException();
        }

        return extension;
    }

    private Optional<File> convertMultipartFileToFile(MultipartFile file) throws IOException {
        File convertFile = new File(Objects.requireNonNull(file.getOriginalFilename()));
        if(convertFile.createNewFile()) {
            try(FileOutputStream fileOutputStream = new FileOutputStream(convertFile)) {
                fileOutputStream.write(file.getBytes());
            }
            return Optional.of(convertFile);
        }

        return Optional.empty();
    }

    private String saveImage(File file, String extension) {
        String filePath = baseImageUrl + UUID.randomUUID() + extension;
        amazonS3Client.putObject(new PutObjectRequest(bucketName, filePath, file)
                .withCannedAcl(CannedAccessControlList.PublicRead));

        return filePath;
    }

}
