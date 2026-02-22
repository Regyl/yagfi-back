package com.github.regyl.gfi.feign;

import com.github.regyl.gfi.dto.external.pypi.PypiResponseDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "pypiClient", url = "https://pypi.org")
public interface PypiClient {

    @GetMapping("/pypi/{package}/json")
    PypiResponseDto getPackage(@PathVariable("package") String packageName);
}
