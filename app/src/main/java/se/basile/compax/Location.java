package se.basile.compax;

import android.widget.ImageView;
import android.widget.TextView;

import java.util.Observable;
import java.util.Observer;

import se.basile.compax.nmea.GPRMC;

public class Location implements Observer {
    private ImageView locationImageView;
    private TextView locationTextView;

    public Location(ImageView imageView, TextView textView){
        locationImageView = imageView;
        locationTextView = textView;
    }

    public boolean isShown(){
        return locationImageView.isShown();
    }

    public void setVisibility(int visibility){
        locationImageView.setVisibility(visibility);
        locationTextView.setVisibility(visibility);
    }

    @Override
    public void update(Observable observable, Object sentence) {
        GPRMC gprmc = (GPRMC) sentence;

        locationTextView.setText("Location\n"+ gprmc.lat +"\n"+gprmc.lon);
    }

}
