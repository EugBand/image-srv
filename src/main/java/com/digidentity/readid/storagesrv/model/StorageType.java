package com.digidentity.readid.storagesrv.model;

import lombok.Getter;

@Getter
public enum StorageType {
    STAGING ("staging"),
    PERMANENT ("permanent"),
    RESERVE ("reserve");

    private final String buckedName;

    StorageType(String buckedName) {
        this.buckedName = buckedName;
    }
}
