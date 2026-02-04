package com.github.regyl.yagfi.controller;

import org.springframework.core.io.ClassPathResource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.List;

@RestController
@RequestMapping("/api/labels")
public class LabelController {

    @GetMapping
    public ResponseEntity<List<String>> getSupportedLabels() throws IOException {
        var resource = new ClassPathResource("data/labels.txt");

        List<String> lines = Files.readAllLines(
                resource.getFile().toPath(),
                StandardCharsets.UTF_8
        );

        List<String> labels = lines.stream()
                .map(String::trim)
                .filter(line -> !line.isBlank() && !line.startsWith("#"))
                .toList();

        return ResponseEntity.ok(labels);
    }
}