package com.tenpo.testtenpo.domain.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import static com.tenpo.testtenpo.shared.Constants.NUMBER_ONE_MANDATORY;
import static com.tenpo.testtenpo.shared.Constants.NUMBER_TWO_MANDATORY;

@Data
public class CalculateRequestDto {
    @NotNull(message = NUMBER_ONE_MANDATORY)
    private Double numberOne;
    @NotNull(message = NUMBER_TWO_MANDATORY)
    private Double numberTwo;
}
