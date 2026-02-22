package com.github.regyl.gfi.feign;

import com.github.regyl.gfi.dto.external.crates.CratesIoResponseDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "cratesIoClient", url = "https://crates.io/api/v1")
public interface CratesIoClient {

    @GetMapping("/crates/{crate}")
    CratesIoResponseDto getCrate(@PathVariable("crate") String crate);
}
