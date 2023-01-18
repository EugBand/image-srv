package com.digidentity.readid.storagesrv.mapper;

import com.digidentity.readid.storagesrv.dto.MrzImageDto;
import com.digidentity.readid.storagesrv.model.MrzImage;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface MrzImageMapper {

    MrzImageDto toDto(MrzImage resource);

    MrzImage toModel(MrzImageDto resourceDto);

}
