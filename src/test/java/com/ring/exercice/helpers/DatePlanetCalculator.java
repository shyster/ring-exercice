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
        long earthSecond = (long) (SOL_DAY * sol * 86400); //day to seconds
        LocalDateTime ldt = LocalDateTime.parse(CURIOSITY_LANDING_DATE).plusSeconds(earthSecond);
        return Date.from(ldt.atZone(ZoneOffset.UTC).toInstant());
    }
}
