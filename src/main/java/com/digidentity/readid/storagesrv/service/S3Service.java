package com.digidentity.readid.storagesrv.service;

import static com.digidentity.readid.storagesrv.service.StorageService.ERROR_DOWNLOADING_FILE;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectListing;
import com.amazonaws.services.s3.model.S3Object;
import com.digidentity.readid.storagesrv.exception.BadRequestException;
import com.digidentity.readid.storagesrv.exception.FileProcessingException;
import java.io.File;
import java.io.IOException;
import javax.validation.constraints.NotNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@Slf4j
public class S3Service {

    public static final String JAVA_IO_TMPDIR = "java.io.tmpdir";

    private final AmazonS3 amazonS3;

    @Autowired
    public S3Service(AmazonS3 amazonS3) {
        this.amazonS3 = amazonS3;
    }

    //    @Value("${s3.mrz.extension}")
    private static final String fileExtension = ".jpg";

    public String upLoadFile(MultipartFile mrzFile, String mrzId, String s3BucketName, String path) throws IOException {
        log.info("Start uploading file with id {}", mrzId);
        String fullPath = getFilePath(mrzId, path);
        File file = multipartToFile(mrzFile, mrzId + fileExtension);
        amazonS3.putObject(s3BucketName, fullPath, file);
        log.info("File with id {} uploaded", mrzId);
        return mrzId;
    }

    public byte[] downLoadFile(String resourceId, String s3BucketName, String path) {
        log.info("Start downloading file with id {}", resourceId);
        String fullPath = getFilePath(resourceId, path);
        ObjectListing objectListing = amazonS3.listObjects(s3BucketName);
        if (objectListing.getObjectSummaries().stream().anyMatch(item -> item.getKey().equals(fullPath))) {
            S3Object s3object = amazonS3.getObject(s3BucketName, fullPath);
            try {
                byte[] bytes = s3object.getObjectContent().getDelegateStream().readAllBytes();
                log.info("File with id {} downloaded", resourceId);
                return bytes;
            } catch (IOException e) {
                log.error("Cant read file: {} from bucket: {}", fullPath, s3BucketName);
                throw new FileProcessingException(ERROR_DOWNLOADING_FILE + e.getMessage(), e);
            }
        }
        throw new BadRequestException(String.format("File with id: %s in S3 repo not dound", resourceId));
    }

    public void moveFile(String s3FileName, String oldBucket, String newBucket){
        amazonS3.copyObject(oldBucket, s3FileName, newBucket, s3FileName);
        log.info("File with name {} downloaded", s3FileName);
    }

    public String deleteFile(String resourceId, String s3BucketName, String path){
        log.info("Start deleting file with id {}", resourceId);
        amazonS3.deleteObject(s3BucketName, getFilePath(resourceId, path));
        log.info("File with id {} deleted", resourceId);
        return resourceId + fileExtension;
    }

    private String getFilePath(String resourceId, String s3Folder){
        return s3Folder + File.separator + resourceId + fileExtension;
    }


    @NotNull
    private File multipartToFile(MultipartFile multipart, String fileName)
            throws IOException {
        File convFile = new File(System.getProperty(JAVA_IO_TMPDIR) + File.separator + fileName);
        log.info("Created temporary file {}", convFile.getPath());
        multipart.transferTo(convFile);
        return convFile;
    }
}
