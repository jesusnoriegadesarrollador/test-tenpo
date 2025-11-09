package com.tenpo.testtenpo.application.service.impl;

import com.tenpo.testtenpo.application.cache.PercentageCache;
import com.tenpo.testtenpo.application.service.PercentageService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PercentageServiceImpl implements PercentageService {

    private final PercentageCache percentageCache;

    /**
     * Obtiene el porcentaje de un "servicio externo" (simulado)
     * @return porcentaje como double
     * @throws RuntimeException si no hay valor disponible
     */
    public double getPercentage() {
        try {
            // SE LLAMA EL METODO QUE SIMULA EL LLAMADO EXTERNO DE UN SERVICIO
            double externalPercentage = fetchFromExternalService();
            // SE PROCEDE ACTUALIZAR LA CACHE
            percentageCache.updateCache(externalPercentage);
            return externalPercentage;
        } catch (Exception e) {
            // SI OCURRE LA EXCEPTION SE USA EL ULTIMO VALOR QUE ESTA EN LA CACHE
            Double cached = percentageCache.getCache();
            if (cached != null) {
                return cached;
            } else {
                throw new RuntimeException("No se pudo obtener el porcentaje del servicio externo ni de la caché.");
            }
        }
    }

    /**
     * Simulación de llamada a un servicio externo
     */
    private double fetchFromExternalService() throws InterruptedException {
        return 0.05;
    }
}