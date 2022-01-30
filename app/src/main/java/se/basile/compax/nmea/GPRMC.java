package se.basile.compax.nmea;

public class GPRMC extends SentenceAbstr {
    public String lat;
    public String lon;
    public String soc;
    public String tmg;

    public GPRMC(){
        type = "GPRMC";
    }
    public void parse(String [] tokens) {
        lat = Utils.Latitude(tokens[3], tokens[4]);
        lon = Utils.Longitude(tokens[5], tokens[6]);
        soc = tokens[7];
        tmg = tokens[8];

        setChanged();
        notifyObservers(this);
    }

    public String toString() {
        String ts = Utils.getCurrentLocalTimeStamp();

        return String.format(ts+" (R)GPRMC: SOG %s - TMG %s", soc, tmg);

    }
}
