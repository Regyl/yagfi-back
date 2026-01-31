package com.github.regyl.gfi.controller;

import com.github.regyl.gfi.service.cyclonedx.CycloneDxProxyService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/test")
@RequiredArgsConstructor //FIXME DELETE ME
public class TestController {

    private final CycloneDxProxyService cycloneDxProxyService;

    @GetMapping
    public void test() {
        cycloneDxProxyService.anyAlive();
    }
}
