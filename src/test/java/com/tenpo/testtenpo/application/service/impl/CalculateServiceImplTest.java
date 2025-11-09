package com.tenpo.testtenpo.application.service.impl;


import com.tenpo.testtenpo.domain.dto.CalculateResponseDto;
import com.tenpo.testtenpo.infrastructure.logging.AsyncLog;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class CalculateServiceImplTest {

    private CalculateServiceImpl calculateService;
    private PercentageServiceImpl percentageService;
    private AsyncLog asyncLogger;

    @BeforeEach
    void setUp() {
        percentageService = mock(PercentageServiceImpl.class);
        asyncLogger = mock(AsyncLog.class);
        calculateService = new CalculateServiceImpl(percentageService, asyncLogger);
    }

    @Test
    void testCalculate() throws Exception {
        //EL PORCENTAJE PARA EL TEST SERIA 10%
        when(percentageService.getPercentage()).thenReturn(0.1);

        CalculateResponseDto response = calculateService.calculate(100, 100);

        //EL VALOR CALCULAOD PARA EL RES SERIA 220
        double expected = 100 + 100 + ((100 + 100) * 0.1);
        assertEquals(expected, response.getResult());
        verify(asyncLogger, times(1)).log(eq("CALCULATION"), anyString(), eq("SUCCESS"));
    }

    @Test
    void testCalculateWithExternalServiceFailureAndCache() throws Exception {

        when(percentageService.getPercentage()).thenThrow(new RuntimeException("Service down"));

        //SIMULAR QUE LA CACHE DEVUELVE 10% Y EN CASO QUE AUN NO HALLA CACHE SE DEBE DEVOLVER LA EXCEPTION
        try {
            calculateService.calculate(100, 100);
        } catch (Exception e) {
            assertEquals("Service down", e.getMessage());
        }

        verify(asyncLogger, times(1)).log(eq("CALCULATION"), contains("Service down"), eq("ERROR"));
    }
}
