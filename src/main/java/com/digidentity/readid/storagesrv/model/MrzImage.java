package com.digidentity.readid.storagesrv.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@RedisHash("Student")
public class MrzImage {

    @Id
    private String id;
    private String name;
//    private StorageType storageType;
    private String bucketName;
}
