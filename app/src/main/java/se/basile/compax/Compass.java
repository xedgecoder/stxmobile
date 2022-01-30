package se.basile.compax;

import android.widget.ImageView;
import android.widget.TextView;

import java.util.Observable;
import java.util.Observer;

import se.basile.compax.nmea.ECRMB;
import se.basile.compax.nmea.GPRMC;
import se.basile.compax.nmea.YDHDM;

public class Compass implements Observer {
    private ImageView compass_img;
    private TextView txt_compass;

    public Compass(ImageView compass_img, TextView txt_compass){
        this.compass_img = compass_img;
        this.txt_compass = txt_compass;
    }
    @Override
    public void update(Observable observable, Object sentence) {
        float heading = 0.f;
        YDHDM ydhdm = (YDHDM) sentence;

        heading = Float.parseFloat(ydhdm.heading);
        compass_img.setRotation(-heading);

        String where = "NW";

        if (heading >= 350 || heading <= 10)
            where = "N";
        if (heading < 350 && heading > 280)
            where = "NW";
        if (heading <= 280 && heading > 260)
            where = "W";
        if (heading <= 260 && heading > 190)
            where = "SW";
        if (heading <= 190 && heading > 170)
            where = "S";
        if (heading <= 170 && heading > 100)
            where = "SE";
        if (heading <= 100 && heading > 80)
            where = "E";
        if (heading <= 80 && heading > 10)
            where = "NE";

        txt_compass.setText(Math.round(heading) + "° " + where);
    }
}
