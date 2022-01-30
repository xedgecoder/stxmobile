package se.basile.compax.nmea;

public class YDHDM extends SentenceAbstr {
    public String heading;

    public YDHDM(){
        type = "YDHDM";
    }
    public void parse(String [] tokens) {
        heading = tokens[1];

        setChanged();
        notifyObservers(this);
    }

    public String toString() {
        String ts = Utils.getCurrentLocalTimeStamp();

        return String.format(ts+" (R)YDHDM: HDG %s ", heading);
    }

}