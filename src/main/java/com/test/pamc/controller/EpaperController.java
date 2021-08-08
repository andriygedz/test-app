package com.test.pamc.controller;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.xml.bind.JAXBException;

import org.hibernate.query.QueryParameter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.test.pamc.entity.EpaperEntity;
import com.test.pamc.service.EpaperService;

import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;

@RestController
@RequestMapping("/epaper")
public class EpaperController {
    private static final Logger logger = LoggerFactory.getLogger(EpaperController.class);

    private final EpaperService epaperService;

    public EpaperController(EpaperService epaperService) {
        this.epaperService = epaperService;
    }


    @ApiImplicitParams({
            @ApiImplicitParam(name = "newspaperName", dataTypeClass = String.class, paramType = "query",
                    value = "Filter by NewspaperName"),
            @ApiImplicitParam(name = "page", dataTypeClass = Integer.class, paramType = "query",
                    value = "Results page you want to retrieve (0..N)", example = "1"),
            @ApiImplicitParam(name = "size", dataTypeClass = Integer.class, paramType = "query",
                    value = "Number of records per page.", example = "10"),
            @ApiImplicitParam(name = "sort", allowMultiple = true, dataTypeClass = String.class, paramType = "query",
                    value = "Sorting criteria in the format: property(,asc|desc). " +
                            "Default sort order is ascending. " +
                            "Multiple sort criteria are supported.")
    })
    @GetMapping()
    public Page<EpaperEntity> findByNewspaperName(@RequestParam(required = false) String newspaperName, Pageable pageable) {
        return epaperService.findByNewspaperName(newspaperName, pageable);
    }


    @PostMapping("/upload")
    @ResponseStatus(HttpStatus.CREATED)
    public void handleXMLPostRequest(@RequestPart  MultipartFile file) throws JAXBException, IOException {
        epaperService.createEpaper(file);
    }

    @ExceptionHandler({JAXBException.class})
    public ResponseEntity<Map<String, Object>> handleException(JAXBException e) {
        logger.error("Fail to parse file: ", e);
        Map<String, Object> responseMap = new LinkedHashMap<>();

        if (e.getMessage() != null) {
            responseMap.put("error", e.getMessage());
        } else if (e.getCause() != null) {
            responseMap.put("error", e.getCause().getMessage());
        }
        return new ResponseEntity<>(responseMap, HttpStatus.BAD_REQUEST);
    }

}
