package com.tenpo.testtenpo.infrastructure.controller;

import com.tenpo.testtenpo.application.service.impl.HistoryServiceImpl;
import com.tenpo.testtenpo.domain.dto.HistoryResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/history")
@RequiredArgsConstructor
public class HistoryController {

    private final HistoryServiceImpl historyService;

    @GetMapping
    public Page<HistoryResponseDto> getHistory(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        Pageable pageable = PageRequest.of(page, size);
        return historyService.getHistory(pageable);
    }
}
