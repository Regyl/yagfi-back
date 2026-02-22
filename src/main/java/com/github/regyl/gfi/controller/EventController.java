package com.github.regyl.gfi.controller;

import com.github.regyl.gfi.dto.response.EventResponseDto;
import com.github.regyl.gfi.service.issueload.issuesync.EventService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/events")
public class EventController {

    private final EventService eventService;

    @GetMapping
    public List<EventResponseDto> getAllSyncHistory() {
        return eventService.getAllSyncHistory();
    }

    @GetMapping("/{source}")
    public EventResponseDto getSyncHistoryBySource(@PathVariable String source) {
        return eventService.getSyncHistoryBySource(source);
    }
}
