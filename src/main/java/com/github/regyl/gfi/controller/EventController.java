package com.github.regyl.gfi.controller;

import com.github.regyl.gfi.controller.dto.response.EventResponseDto;
import com.github.regyl.gfi.repository.EventRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/events")
public class EventController {

    private final EventRepository eventRepository;

    @GetMapping("/last")
    public EventResponseDto getLastEvent() {
        return eventRepository.findLatestEvent();
    }
}
