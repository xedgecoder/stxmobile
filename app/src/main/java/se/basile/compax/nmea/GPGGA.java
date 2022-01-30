package se.basile.compax.nmea;

public class GPGGA extends SentenceAbstr {
    public String lat;
    public String lon;

    public GPGGA(){
        type = "GPGGA";
    }
    public void parse(String [] tokens) {
        lat = Utils.Latitude(tokens[2], tokens[3]);
        lon = Utils.Longitude(tokens[4], tokens[5]);

        setChanged();
        notifyObservers(this);
    }

    public String toString() {
        String ts = Utils.getCurrentLocalTimeStamp();

        return String.format(ts+" (R)GPGGA: LAT %s - LON %s", lat, lon);
    }
}
