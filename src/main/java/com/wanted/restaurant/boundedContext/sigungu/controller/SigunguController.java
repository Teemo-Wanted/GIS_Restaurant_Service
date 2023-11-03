package com.wanted.restaurant.boundedContext.sigungu.controller;

import com.wanted.restaurant.base.rsData.RsData;
import com.wanted.restaurant.boundedContext.sigungu.service.SigunguService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/locations")
@RequiredArgsConstructor
public class SigunguController {

    private final SigunguService sigunguService;
    @PostMapping("/init")
    public RsData resetRegionList() {
        return sigunguService.initSigunguData();
    }
}
