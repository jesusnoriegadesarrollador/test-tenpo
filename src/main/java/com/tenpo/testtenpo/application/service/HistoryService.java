package com.tenpo.testtenpo.application.service;

import com.tenpo.testtenpo.domain.dto.HistoryResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface HistoryService {

    public Page<HistoryResponseDto> getHistory(Pageable pageable);

}
