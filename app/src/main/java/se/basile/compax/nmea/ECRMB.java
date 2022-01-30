package se.basile.compax.nmea;

public class ECRMB extends SentenceAbstr {
    public String crossTrackError;
    public String range;
    public String bearing;

    public ECRMB(){
        type = "ECRMB";
    }
    public void parse(String [] tokens) {
        crossTrackError = tokens[2];
        range = tokens[10];
        bearing = tokens[11];

        setChanged();
        notifyObservers(this);
    }

    public String toString() {
        String ts = Utils.getCurrentLocalTimeStamp();

        return String.format(ts+" (R)ECRMB: ZTE %s - BRG %s - RNG %s", crossTrackError, bearing, range);
    }

}
