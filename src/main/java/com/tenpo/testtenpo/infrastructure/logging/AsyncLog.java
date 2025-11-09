package com.tenpo.testtenpo.infrastructure.logging;

import com.tenpo.testtenpo.domain.model.HistoryEntity;
import com.tenpo.testtenpo.infrastructure.repository.HistoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
public class AsyncLog {

    private final HistoryRepository historyRepository;

    /**
     * Registra una acción de manera asíncrona en la base de datos
     * @param endpoint nombre del endpoint o servicio
     * @param requestPayload parámetros o mensaje de la llamada
     * @param status SUCCESS o ERROR
     */
    @Async
    public void log(String endpoint, String requestPayload, String status) {
        try {
            HistoryEntity history = new HistoryEntity();
            history.setEndpoint(endpoint);
            history.setRequest(requestPayload);
            history.setResponse(status);
            history.setTimestamp(LocalDateTime.now());
            historyRepository.save(history);
        } catch (Exception e) {
            // En caso de error, se podría registrar en un log externo
            System.err.println("AsyncLogger error: " + e.getMessage());
        }
    }
}