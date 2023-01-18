package com.digidentity.readid.storagesrv.controller;

import com.digidentity.readid.storagesrv.dto.MrzImageDto;
import com.digidentity.readid.storagesrv.service.StorageService;
import com.digidentity.readid.storagesrv.validation.UUID;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping(path = "/api/v1/images", produces = MediaType.APPLICATION_JSON_VALUE)
public class ImageController {

    @Autowired StorageService service;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public MrzImageDto addMrz(@RequestParam("id") @UUID String id, @RequestParam("file") MultipartFile file) throws IOException {
        return service.createMrzImage(id, file);
    }


    @GetMapping("{id}")
    public MrzImageDto getMrz(@PathVariable String id) {
        return service.getMrzImage(id);
    }

    @DeleteMapping
    public String deleteMrz(@RequestParam String id) {
        return service.deleteMrzImage(id);
    }

}
