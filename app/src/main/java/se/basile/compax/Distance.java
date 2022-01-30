package se.basile.compax;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Observable;
import java.util.Observer;

import se.basile.compax.nmea.ECRMB;

public class Distance implements Observer {
    private ImageView distanceImageView;
    private TextView distanceTextView;

    public Distance(ImageView imageView, TextView textView){
        distanceImageView = imageView;
        distanceTextView = textView;
    }

    public boolean isShown(){
        return distanceImageView.isShown();
    }

    public void setVisibility(int visibility){
        distanceImageView.setVisibility(visibility);
        distanceTextView.setVisibility(visibility);
    }

    @Override
    public void update(Observable observable, Object sentence) {
        ECRMB ecrmb = (ECRMB) sentence;

        distanceTextView.setText("Distance\n"+ ecrmb.range+" nm");
    }

}
