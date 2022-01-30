package se.basile.compax.nmea;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;

public abstract class SentenceAbstr extends Observable {
    protected String type;

    abstract public void parse(String [] tokens);
    abstract public String toString();

    public String getType(){
        return type;
    }
}
