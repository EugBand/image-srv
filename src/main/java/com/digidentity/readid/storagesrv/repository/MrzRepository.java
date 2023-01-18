package com.digidentity.readid.storagesrv.repository;

import com.digidentity.readid.storagesrv.model.MrzImage;
import java.util.Map;
import java.util.Optional;
import org.springframework.data.repository.CrudRepository;

public interface MrzRepository extends CrudRepository<MrzImage, String> {

    void deleteById(String id);
}
