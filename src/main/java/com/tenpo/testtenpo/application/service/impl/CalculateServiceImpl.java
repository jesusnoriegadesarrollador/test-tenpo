package com.tenpo.testtenpo.application.service.impl;

import com.tenpo.testtenpo.application.service.CalculateService;
import com.tenpo.testtenpo.domain.dto.CalculateResponseDto;
import com.tenpo.testtenpo.infrastructure.logging.AsyncLog;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CalculateServiceImpl implements CalculateService {

    private final PercentageServiceImpl percentageService;
    private final AsyncLog asyncLogger;

    /**
     * Calcula la suma de numberOne + numberTwo y aplica porcentaje dinámico
     * @param numberOne primer número
     * @param numberTwo segundo número
     * @return resultado final con porcentaje
     */
    public CalculateResponseDto calculate(double numberOne, double numberTwo) {
        try {
            double percentage = percentageService.getPercentage();
            double sum = numberOne + numberTwo;
            double result = sum + (sum * percentage);

            asyncLogger.log("CALCULATION", "numberOne=" + numberOne + ", numberTwo=" + numberTwo, "SUCCESS");
            return new CalculateResponseDto(result);
        } catch (Exception e) {
            asyncLogger.log("CALCULATION", "numberOne=" + numberOne + ", numberTwo=" + numberTwo + ", error=" + e.getMessage(), "ERROR");
            throw e;
        }
    }
}