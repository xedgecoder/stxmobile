package se.basile.compax;

import android.widget.ImageView;
import android.widget.TextView;

import java.util.Observable;
import java.util.Observer;

import se.basile.compax.nmea.ECRMB;
import se.basile.compax.nmea.SentenceAbstr;
import se.basile.compax.nmea.YDHDM;

public class Bearing implements Observer {
    private ImageView bearingImageView;
    private ImageView miniCompassImageView;
    private TextView bearingTextView;

    private float bearing = 0.f;
    private float heading = 0.f;

    public Bearing(ImageView bearingImageView, ImageView miniCompassImageView, TextView bearingTextView){
        this.bearingImageView = bearingImageView;
        this.miniCompassImageView = miniCompassImageView;
        this.bearingTextView = bearingTextView;
    }

    public void setRotationRelative(float heading){
        this.heading = heading;
        bearingImageView.setRotation(bearing-heading);
    }

    public boolean isShown(){
        return miniCompassImageView.isShown();
    }


    public void setVisibility(int visibility){
        miniCompassImageView.setVisibility(visibility);
        bearingTextView.setVisibility(visibility);
    }
    @Override
    public void update(Observable observable, Object object) {
        float newBearing = 0.f;
        SentenceAbstr sentence = (SentenceAbstr) object;

        if (sentence.getType()=="ECRMB") {
            ECRMB ecrmb = (ECRMB) sentence;
            newBearing = Math.round(Float.parseFloat(ecrmb.bearing));

            if (newBearing != bearing){
                bearing = newBearing;
                setRotationRelative(heading);

                bearingTextView.setText("Bearing\n"+ bearing+"°");

            }
        } else if (sentence.getType()=="YDHDM") {
            YDHDM ydhdm = (YDHDM) sentence;

            setRotationRelative(Float.parseFloat(ydhdm.heading));

        }

    }

}
