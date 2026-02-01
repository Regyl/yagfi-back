package com.github.regyl.gfi.controller;

import com.github.regyl.gfi.service.cyclonedx.CycloneDxService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/test")
@RequiredArgsConstructor //FIXME DELETE ME
public class TestController {

    private final CycloneDxService cycloneDxService;

    @GetMapping
    public void test() {
        cycloneDxService.anyAlive();
    }
}
