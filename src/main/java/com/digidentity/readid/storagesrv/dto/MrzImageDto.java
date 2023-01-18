package com.digidentity.readid.storagesrv.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.jackson.Jacksonized;

@Data
@Builder
@Jacksonized
@NoArgsConstructor
@AllArgsConstructor
public class MrzImageDto {

    @Builder.Default
    private String id = "";
//    private StorageType storageType;
    private String name;
    private String bucketName;
    @Builder.Default
    private byte[] jpgData = new byte[]{};

}
