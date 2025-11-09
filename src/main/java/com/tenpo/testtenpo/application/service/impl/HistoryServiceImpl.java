package com.tenpo.testtenpo.application.service.impl;

import com.tenpo.testtenpo.application.service.HistoryService;
import com.tenpo.testtenpo.domain.dto.HistoryResponseDto;
import com.tenpo.testtenpo.infrastructure.repository.HistoryRepository;
import com.tenpo.testtenpo.domain.model.HistoryEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class HistoryServiceImpl implements HistoryService {

    private final HistoryRepository historyRepository;

    public Page<HistoryResponseDto> getHistory(Pageable pageable) {
        return historyRepository.findAll(pageable)
                .map(this::toResponse);
    }

    private HistoryResponseDto toResponse(HistoryEntity history) {
        return HistoryResponseDto.builder()
                .id(history.getId())
                .timestamp(history.getTimestamp())
                .endpoint(history.getEndpoint())
                .request(history.getRequest())
                .response(history.getResponse())
                .error(history.getError())
                .build();
    }
}
