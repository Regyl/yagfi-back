package com.github.regyl.gfi.controller;

import com.github.regyl.gfi.service.feed.CycloneDxService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Profile("local")
@RequestMapping("/test")
@RequiredArgsConstructor
public class TestController {

    private final CycloneDxService cycloneDxService;

    @GetMapping
    public void test() {
        cycloneDxService.anyAlive();
    }
}
