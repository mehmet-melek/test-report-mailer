package com.melek;

import java.time.Duration;

public class Util {

    public static String getFormattedDuration(Long durationMs) {

        // Saat, dakika, saniye ve kalan milisaniyeyi hesapla
        long hours = durationMs / (1000 * 60 * 60);
        long minutes = (durationMs % (1000 * 60 * 60)) / (1000 * 60);
        long seconds = (durationMs % (1000 * 60)) / 1000;
        long millis = durationMs % 1000;

        // Formatı oluştur
        return String.format("%dh %dm %ds + %dms", hours, minutes, seconds, millis);
    }
}
