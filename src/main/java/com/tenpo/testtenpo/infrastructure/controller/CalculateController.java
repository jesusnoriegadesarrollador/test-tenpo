package com.tenpo.testtenpo.infrastructure.controller;

import com.tenpo.testtenpo.application.service.CalculateService;
import com.tenpo.testtenpo.domain.dto.CalculateRequestDto;
import com.tenpo.testtenpo.domain.dto.CalculateResponseDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class CalculateController {

    private final CalculateService calculateService;

    @PostMapping("/calculate")
    @Operation(summary = "Suma dos números y aplica un porcentaje dinámico")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Cálculo realizado exitosamente"),
            @ApiResponse(responseCode = "500", description = "Error al calcular el porcentaje")
    })
    public ResponseEntity<CalculateResponseDto> calculate(@Valid @RequestBody CalculateRequestDto request) {
        CalculateResponseDto response = calculateService.calculate(request.getNumberOne(), request.getNumberTwo());
        return ResponseEntity.ok(response);
    }

}