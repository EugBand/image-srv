package com.digidentity.readid.storagesrv.service;

import com.digidentity.readid.storagesrv.dto.MrzImageDto;
import com.digidentity.readid.storagesrv.exception.BadRequestException;
import com.digidentity.readid.storagesrv.exception.FileProcessingException;
import com.digidentity.readid.storagesrv.mapper.MrzImageMapper;
import com.digidentity.readid.storagesrv.model.MrzImage;
import com.digidentity.readid.storagesrv.model.StorageType;
import java.io.IOException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@Slf4j
public class StorageService {

    private static final String BUCKET_NAME = StorageType.PERMANENT.getBuckedName();
    private static final String PATH = "";


    public static final String ERROR_UPLOADING_FILE = "Error uploading file: ";

    public static final String ERROR_DOWNLOADING_FILE = "Error downloading file: ";

    public static final String IS_NOT_OF_MP_3_FORMAT = "Provided file is not of jpeg format";

    @Autowired DBService dbService;

    @Autowired S3Service s3FileService;

    @Autowired MrzImageMapper mapper;

    public MrzImageDto createMrzImage(String id, MultipartFile multipartFile) {
        if(!validateIfJgpFile(multipartFile)) {
            throw new BadRequestException(IS_NOT_OF_MP_3_FORMAT);
        }
        MrzImage mrzImage = MrzImage.builder()
                .name(id)
                .bucketName(BUCKET_NAME)
                .build();
        dbService.saveMrz(mrzImage);
        String mrzId;
        try {
            mrzId = s3FileService.upLoadFile(multipartFile, id, BUCKET_NAME, PATH);
            log.info("File with id {} uploaded" , mrzId);
        } catch (IOException e) {
            log.error(ERROR_UPLOADING_FILE + e.getMessage());
            deleteMrzImage(id);
            throw new FileProcessingException(ERROR_UPLOADING_FILE + e.getMessage(), e);
        }
        MrzImageDto mrzImageDto = mapper.toDto(mrzImage);
        log.info("mrz created with path: {}", mrzId);
        return mrzImageDto;
    }

    public MrzImageDto getMrzImage(String id) {
        MrzImage mrzImage = dbService.getMrzData(id);
        MrzImageDto mrzImageDto = mapper.toDto(mrzImage);
        String mrzId = mrzImage.getId();
        byte[] image = s3FileService.downLoadFile(mrzId, BUCKET_NAME, PATH);
        mrzImageDto.setJpgData(image);
        log.info("Get mrzImage for id: {}", id);
        return mrzImageDto;
    }

    public String deleteMrzImage(String id) {
        log.info("Start deleting mrzs metadata for id: {}", id);
        String deletedMrzId = dbService.deleteMrzData(id);
        s3FileService.deleteFile(id, BUCKET_NAME, PATH);
        return deletedMrzId;
    }

    //TODO
    private boolean validateIfJgpFile(final MultipartFile file) {
        return true;
    }
}
