package se.basile.compax.nmea;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Utils {
    // utils
    static float Latitude2Decimal(String lat, String NS) {
        float med = Float.parseFloat(lat.substring(2))/60.0f;
        med +=  Float.parseFloat(lat.substring(0, 2));
        if(NS.startsWith("S")) {
            med = -med;
        }
        return med;
    }

    static String Latitude(String lat, String NS) {
        if (lat.isEmpty()) {
            return " - ";
        } else {
            return String.format("%s°%s'%s %s", lat.substring(0, 2), lat.substring(2, 4), lat.substring(4, 8), NS);
        }
    }

    static String Longitude(String lon, String NS) {
        if (lon.isEmpty()) {
            return " - ";
        } else {
            return String.format("%s°%s'%s %s", lon.substring(0, 3).replaceAll("^0+(?!$)", ""),lon.substring(3, 5),lon.substring(5, 9),NS);
        }
    }


    static float Longitude2Decimal(String lon, String WE) {
        float med = Float.parseFloat(lon.substring(3))/60.0f;
        med +=  Float.parseFloat(lon.substring(0, 3));
        if(WE.startsWith("W")) {
            med = -med;
        }
        return med;
    }

    static public String getCurrentLocalTimeStamp() {
        return LocalDateTime.now()
                .format(DateTimeFormatter.ofPattern("HH:mm:ss.SSS"));
    }
}
