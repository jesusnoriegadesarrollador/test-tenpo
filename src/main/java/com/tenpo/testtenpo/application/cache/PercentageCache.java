package com.tenpo.testtenpo.application.cache;

import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class PercentageCache {

    private Double cachedPercentage;
    private LocalDateTime cacheTime;
    private static final long CACHE_DURATION_MINUTES = 30;

    /**
     * Obtiene el valor almacenado en caché si no ha expirado.
     * @return porcentaje en caché o null si no existe o expiró
     */
    public synchronized Double getCache() {
        if (cachedPercentage == null || cacheTime == null) {
            return null;
        }
        LocalDateTime now = LocalDateTime.now();
        if (cacheTime.plusMinutes(CACHE_DURATION_MINUTES).isBefore(now)) {
            cachedPercentage = null;
            cacheTime = null;
            return null;
        }
        return cachedPercentage;
    }

    /**
     * Actualiza la caché con un nuevo valor y marca el timestamp
     * @param percentage nuevo valor del porcentaje
     */
    public synchronized void updateCache(double percentage) {
        this.cachedPercentage = percentage;
        this.cacheTime = LocalDateTime.now();
    }
}