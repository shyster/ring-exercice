package com.ring.exercice.helpers;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Date;

import static com.ring.exercice.core.Constants.CURIOSITY_LANDING_DATE;
import static com.ring.exercice.core.Constants.SOL_DAY;

/**
 * Created by Vladislav Kulasov on 28.01.2018.
 */
public class DatePlanetCalculator {

    public static Date getCuriosityEarthDateBySol(long sol) {
        long earthDays = (long) (SOL_DAY * sol);
        LocalDateTime ldt = LocalDateTime.parse(CURIOSITY_LANDING_DATE).plusDays(earthDays);
        return Date.from(ldt.atZone(ZoneOffset.UTC).toInstant());
    }
}
