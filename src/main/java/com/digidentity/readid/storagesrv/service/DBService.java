package com.digidentity.readid.storagesrv.service;

import com.digidentity.readid.storagesrv.exception.BadRequestException;
import com.digidentity.readid.storagesrv.model.MrzImage;
import com.digidentity.readid.storagesrv.repository.MrzRepository;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class DBService {

    public static final String NOT_FOUND_IN_BD = "MrzImage with id %s not found in bd";

    @Autowired MrzRepository mrzRepository;

    public String saveMrz(MrzImage mrzImage) {
        log.info("Start adding mrzImage to DB with s3 name", mrzImage.getId(), mrzImage.getName());
        mrzRepository.save(mrzImage);
        String id = mrzImage.getId();
        log.info("mrzImage added to DB with id: {}", id);
        return id;
    }

    public MrzImage getMrzData(String id) {
        log.info("Start getting mrz to DB with id {}", id);
        Optional<MrzImage> mrz = mrzRepository.findById(id);
        MrzImage savedMrzImage = mrz.orElseThrow(() -> new BadRequestException(String.format(NOT_FOUND_IN_BD, id)));
        log.info("Get mrz from DB with id: {}", id);
        return savedMrzImage;
    }

    public String deleteMrzData(String id) {
        log.info("Start deleting mrz from DB with id {}", id);
        mrzRepository.deleteById(id);
        log.info("Mrzs metadata deleted from DB with id {}", id);
        return id;
    }
}
