package se.basile.compax;

import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

import se.basile.compax.nmea.SentenceAbstr;


public class DisplayRemote implements Observer {
    private TextView displayTextView;
    private boolean pauseAddText = false;
    List<String> displayRaws = new ArrayList<String>();

    public DisplayRemote(TextView displayTextView){
        this.displayTextView = displayTextView;

        displayTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (pauseAddText)
                    pauseAddText = false;
                else
                    pauseAddText = true;

            }
        });
    }
    public void addText(String text) {
        if(!pauseAddText ) {
            if (displayRaws.size() == 5)
            displayRaws.remove(0);

            displayRaws.add(text);
            displayTextView.setText(String.join("\n", displayRaws));
        }
    }


    @Override
    public void update(Observable observable, Object object) {
        SentenceAbstr sentence = (SentenceAbstr) object;

        addText(sentence.toString());
    }


}
