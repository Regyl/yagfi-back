package com.github.regyl.gfi.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "CycloneDxFeignClient", url = "http://127.0.0.1:9090/sbom")
public interface CycloneDxFeignClient {

    @GetMapping
    Object getProjectSbom(@RequestParam("url") String url);
}
