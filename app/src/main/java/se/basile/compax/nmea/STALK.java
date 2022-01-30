package se.basile.compax.nmea;

import java.util.HashMap;
import java.util.Map;

public class STALK extends SentenceAbstr {
    public String stalkComm;
    private  HashMap<String, String> stalkMap = new HashMap<String, String>();


    public STALK(){
        type = "STALK";
        stalkMap.put("01", "Auto");
        stalkMap.put("02", "Standby");
        stalkMap.put("05", "-1");
        stalkMap.put("06", "-10");
        stalkMap.put("07", "+1");
        stalkMap.put("08", "+10");
        stalkMap.put("21", "Tack port");
        stalkMap.put("22", "Tack starboard");
        stalkMap.put("28", "Tracking");

    }

    @Override
    public void parse(String[] tokens) {
        stalkComm = tokens[3];

        setChanged();
        notifyObservers(this);
    }


    @Override
    public String toString() {
        SentenceAbstr sentence = null;
        String txt = "";
        String ts = Utils.getCurrentLocalTimeStamp();

        if(stalkMap.containsKey(stalkComm)) {
            txt = stalkMap.get(stalkComm);
        }
        return String.format(ts+" (T)STALK: %s", txt);
    }
}
