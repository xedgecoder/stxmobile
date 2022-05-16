package se.basile.compax;

import android.content.Intent;
import android.graphics.Color;
import android.media.AudioManager;
import android.media.ToneGenerator;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import se.basile.compax.nmea.ECRMB;
import se.basile.compax.nmea.GPRMC;
import se.basile.compax.nmea.STALK;
import se.basile.compax.nmea.SentencesDB;
import se.basile.compax.nmea.Utils;
import se.basile.compax.nmea.YDHDM;

public class MainActivity extends AppCompatActivity{
    private Handler h;
    private String status ="standby";

    // Vibrate for 150 milliseconds
    private void playBeepAndshake(int tone, long millisecons) {
        ToneGenerator toneGen1 = new ToneGenerator(AudioManager.STREAM_MUSIC, 100);
        toneGen1.startTone(tone, (int) millisecons);

        if (Build.VERSION.SDK_INT >= 26) {
            ((Vibrator) getSystemService(VIBRATOR_SERVICE)).vibrate(VibrationEffect.createOneShot(millisecons, VibrationEffect.DEFAULT_AMPLITUDE));
        } else {
            ((Vibrator) getSystemService(VIBRATOR_SERVICE)).vibrate(millisecons);
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        View decorView = getWindow().getDecorView();
        // Hide both the navigation bar and the status bar.
        // SYSTEM_UI_FLAG_FULLSCREEN is only available on Android 4.1 and higher, but as
        // a general rule, you should design your app to hide the status bar whenever you
        // hide the navigation bar.
        int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_FULLSCREEN;

        //decorView.setSystemUiVisibility(uiOptions);
        SettingsAsset settingsAssest = new SettingsAsset(getBaseContext());

        DisplayRemote display = new DisplayRemote((TextView) findViewById(R.id.displayTextView));
        Compass compass = new Compass((ImageView) findViewById(R.id.img_compass),(TextView) findViewById(R.id.txt_azimuth) );
        Bearing bearing = new Bearing(
                (ImageView) findViewById(R.id.bearing),
                (ImageView) findViewById(R.id.miniCompassImageView),
                (TextView) findViewById(R.id.bearingTextView));

        bearing.setRotationRelative(-59.0f);

        //Location location = new Location((ImageView) findViewById(R.id.miniCompassImageView),(TextView) findViewById(R.id.locationTextView));
        Distance distance = new Distance((ImageView) findViewById(R.id.distanceImageView), (TextView) findViewById(R.id.distanceTextView));

        GPRMC gprmc = new GPRMC();
        ECRMB ecrmb = new ECRMB();
        YDHDM ydhdm = new YDHDM();
        STALK stalk = new STALK();

        gprmc.addObserver(display);
        ydhdm.addObserver(display);
        ecrmb.addObserver(display);
        stalk.addObserver(display);

        ydhdm.addObserver(compass);
        ydhdm.addObserver(bearing);
        ecrmb.addObserver(bearing);

        ecrmb.addObserver(distance);

        SentencesDB sentencesDB = new SentencesDB();
        sentencesDB.addSentence("GPRMC", gprmc);
        sentencesDB.addSentence("ECRMB", ecrmb);
        sentencesDB.addSentence("YDHDM", ydhdm);
        sentencesDB.addSentence("STALK", stalk);

        Runnable r = new Runnable(){
            @Override
            public void run() {
                h.postDelayed(this, 4000);
                if (bearing.isShown()){
                    bearing.setVisibility(View.INVISIBLE);
                    distance.setVisibility(View.VISIBLE);
                }
                else{
                    bearing.setVisibility(View.VISIBLE);
                    distance.setVisibility(View.INVISIBLE);
                }
            }

        };

        h = new Handler();
        h.post(r);

        TextView statusTextView = (TextView) findViewById(R.id.statusTextView);

        ImageButton tack =  (ImageButton) findViewById(R.id.tack);
        ImageButton tackOff =  (ImageButton) findViewById(R.id.tackOff);
        ImageButton followBtn =  (ImageButton) findViewById(R.id.followBtn);
        ImageButton followOffBtn =  (ImageButton) findViewById(R.id.followBtn);
        ImageButton autoBtn =  (ImageButton) findViewById(R.id.autoBtn);
        ImageButton leftOffImageButton = (ImageButton) findViewById(R.id.leftOffImageButton);
        ImageButton leftTackImageButton = (ImageButton) findViewById(R.id.leftTackImageButton);
        ImageButton leftImageButton = (ImageButton) findViewById(R.id.leftImageButton);
        ImageButton rightOffImageButton = (ImageButton) findViewById(R.id.rightOffImageButton);
        ImageButton rightTackImageButton = (ImageButton) findViewById(R.id.rightTackImageButton);
        ImageButton rightImageButton = (ImageButton) findViewById(R.id.rightImageButton);
        ImageButton settings = (ImageButton) findViewById(R.id.settings);



        tack.setVisibility(View.INVISIBLE);
        followBtn.setVisibility(View.INVISIBLE);

        leftTackImageButton.setVisibility(View.INVISIBLE);

        leftOffImageButton.setVisibility(View.INVISIBLE);
        leftImageButton.setVisibility(View.VISIBLE);

        rightTackImageButton.setVisibility(View.INVISIBLE);

        rightOffImageButton.setVisibility(View.INVISIBLE);
        rightImageButton.setVisibility(View.VISIBLE);

        settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startSettingsActivity();
            }
        });

        tack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String ts = Utils.getCurrentLocalTimeStamp();

                if (statusTextView.getText().toString()== "ready to tack"){
                    statusTextView.setBackgroundResource(R.drawable.status_on);
                    statusTextView.setTextColor(Color.BLACK);
                    statusTextView.setText("auto");
                    playBeepAndshake(ToneGenerator.TONE_CDMA_DIAL_TONE_LITE, 150);

                    leftOffImageButton.setVisibility(View.INVISIBLE);
                    leftImageButton.setVisibility(View.VISIBLE);

                    rightOffImageButton.setVisibility(View.INVISIBLE);
                    rightImageButton.setVisibility(View.VISIBLE);

                    leftTackImageButton.setVisibility(View.INVISIBLE);
                    rightTackImageButton.setVisibility(View.INVISIBLE);

                } else {
                    statusTextView.setBackgroundResource(R.drawable.status_off);
                    statusTextView.setTextColor(Color.parseColor("#FFE3E2E2"));
                    statusTextView.setText("ready to tack");
                    playBeepAndshake(ToneGenerator.TONE_CDMA_DIAL_TONE_LITE, 150);

                    leftOffImageButton.setVisibility(View.INVISIBLE);
                    leftImageButton.setVisibility(View.INVISIBLE);

                    rightOffImageButton.setVisibility(View.INVISIBLE);
                    rightImageButton.setVisibility(View.INVISIBLE);

                    leftTackImageButton.setVisibility(View.VISIBLE);
                    rightTackImageButton.setVisibility(View.VISIBLE);
                }

            }
        });

        leftTackImageButton.setOnClickListener(new View.OnClickListener() {
            private Handler mHandler;
            @Override
            public void onClick(View view) {
                statusTextView.setBackgroundResource(R.drawable.status_on);
                statusTextView.setTextColor(Color.BLACK);
                statusTextView.setText("tack port");
                playBeepAndshake(ToneGenerator.TONE_CDMA_DIAL_TONE_LITE, 150);;

                new Handler().postDelayed(new Runnable(){
                    @Override
                    public void run() {
                        playBeepAndshake(ToneGenerator.TONE_CDMA_DIAL_TONE_LITE, 150);;
                        SystemClock.sleep(1000);
                        playBeepAndshake(ToneGenerator.TONE_CDMA_DIAL_TONE_LITE, 150);;
                        SystemClock.sleep(1000);
                        playBeepAndshake(ToneGenerator.TONE_CDMA_DIAL_TONE_LITE, 1500);

                        String msg = getResources().getString(R.string.tackPort);
                        new Thread(new MsgSender(settingsAssest.get("serverIP"), settingsAssest.get("serverRxPort"), msg)).start();
                        sentencesDB.parse(msg);

                        new Handler().postDelayed(new Runnable(){
                            @Override
                            public void run() {

                                leftTackImageButton.setVisibility(View.INVISIBLE);

                                leftOffImageButton.setVisibility(View.INVISIBLE);
                                leftImageButton.setVisibility(View.VISIBLE);

                                rightTackImageButton.setVisibility(View.INVISIBLE);

                                rightOffImageButton.setVisibility(View.INVISIBLE);
                                rightImageButton.setVisibility(View.VISIBLE);

                                statusTextView.setBackgroundResource(R.drawable.status_on);
                                statusTextView.setTextColor(Color.BLACK);
                                statusTextView.setText("auto");
                                tack.setVisibility(View.VISIBLE);
                                followBtn.setVisibility(View.VISIBLE);

                            }
                        }, 1500);


                    }

                }, 1000);
            }
        });

        rightTackImageButton.setOnClickListener(new View.OnClickListener() {
            private Handler mHandler;
            @Override
            public void onClick(View view) {
                statusTextView.setBackgroundResource(R.drawable.status_on);
                statusTextView.setTextColor(Color.BLACK);
                statusTextView.setText("tack starboard");
                playBeepAndshake(ToneGenerator.TONE_CDMA_DIAL_TONE_LITE, 150);;

                new Handler().postDelayed(new Runnable(){
                    @Override
                    public void run() {
                        playBeepAndshake(ToneGenerator.TONE_CDMA_DIAL_TONE_LITE, 150);;
                        SystemClock.sleep(1000);
                        playBeepAndshake(ToneGenerator.TONE_CDMA_DIAL_TONE_LITE, 150);;
                        SystemClock.sleep(1000);
                        playBeepAndshake(ToneGenerator.TONE_CDMA_DIAL_TONE_LITE, 1500);

                        String msg = getResources().getString(R.string.tackStarboard);
                        new Thread(new MsgSender(settingsAssest.get("serverIP"), settingsAssest.get("serverRxPort"), msg)).start();
                        sentencesDB.parse(msg);

                        new Handler().postDelayed(new Runnable(){
                            @Override
                            public void run() {
                                leftTackImageButton.setVisibility(View.INVISIBLE);

                                leftOffImageButton.setVisibility(View.INVISIBLE);
                                leftImageButton.setVisibility(View.VISIBLE);

                                rightTackImageButton.setVisibility(View.INVISIBLE);

                                rightOffImageButton.setVisibility(View.INVISIBLE);
                                rightImageButton.setVisibility(View.VISIBLE);

                                statusTextView.setBackgroundResource(R.drawable.status_on);
                                statusTextView.setTextColor(Color.BLACK);
                                statusTextView.setText("auto");
                                tack.setVisibility(View.VISIBLE);
                                followBtn.setVisibility(View.VISIBLE);

                            }
                        }, 1500);

                    }

                }, 1000);
            }
        });


        autoBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String ts = Utils.getCurrentLocalTimeStamp();

                if (status=="standby"){
                    leftTackImageButton.setVisibility(View.INVISIBLE);
                    leftOffImageButton.setVisibility(View.INVISIBLE);
                    leftImageButton.setVisibility(View.VISIBLE);

                    rightTackImageButton.setVisibility(View.INVISIBLE);
                    rightOffImageButton.setVisibility(View.INVISIBLE);
                    rightImageButton.setVisibility(View.VISIBLE);

                    //auto
                    String msg = getResources().getString(R.string.Auto);
                    new Thread(new MsgSender(settingsAssest.get("serverIP"), settingsAssest.get("serverRxPort"), msg)).start();
                    sentencesDB.parse(msg);

                    statusTextView.setBackgroundResource(R.drawable.status_on);
                    statusTextView.setTextColor(Color.BLACK);
                    statusTextView.setText("auto");
                    tack.setVisibility(View.VISIBLE);
                    followBtn.setVisibility(View.VISIBLE);

                    status = "auto";

                } else if (status=="auto"){
                    leftTackImageButton.setVisibility(View.INVISIBLE);
                    leftOffImageButton.setVisibility(View.INVISIBLE);
                    leftImageButton.setVisibility(View.VISIBLE);

                    rightTackImageButton.setVisibility(View.INVISIBLE);
                    rightOffImageButton.setVisibility(View.INVISIBLE);
                    rightImageButton.setVisibility(View.VISIBLE);

                    //standby
                    String msg = getResources().getString(R.string.Standby);
                    new Thread(new MsgSender(settingsAssest.get("serverIP"), settingsAssest.get("serverRxPort"), msg)).start();
                    sentencesDB.parse(msg);

                    statusTextView.setBackgroundResource(R.drawable.status_off);
                    statusTextView.setTextColor(Color.parseColor("#FFE3E2E2"));
                    statusTextView.setText("standby");
                    tack.setVisibility(View.INVISIBLE);
                    followBtn.setVisibility(View.INVISIBLE);
                    status = "standby";

                }

                Log.d("COMPAX", ts+" auto pressed");

                playBeepAndshake(ToneGenerator.TONE_CDMA_DIAL_TONE_LITE, 150);;
            }
        });

        followBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String ts = Utils.getCurrentLocalTimeStamp();
                leftOffImageButton.setVisibility(View.INVISIBLE);
                leftImageButton.setVisibility(View.VISIBLE);
                leftTackImageButton.setVisibility(View.VISIBLE);
                rightOffImageButton.setVisibility(View.INVISIBLE);
                rightImageButton.setVisibility(View.VISIBLE);
                rightTackImageButton.setVisibility(View.VISIBLE);

                //tracking
                String msg = getResources().getString(R.string.enterTrackMode);
                new Thread(new MsgSender(settingsAssest.get("serverIP"), settingsAssest.get("serverRxPort"), msg)).start();
                sentencesDB.parse(msg);
                if (statusTextView.getText().toString()== "acquiring the track"){
                    statusTextView.setBackgroundResource(R.drawable.status_on);
                    statusTextView.setTextColor(Color.BLACK);
                    statusTextView.setText("tracking");
                    playBeepAndshake(ToneGenerator.TONE_CDMA_DIAL_TONE_LITE, 150);;
                } else {
                    statusTextView.setBackgroundResource(R.drawable.status_off);
                    statusTextView.setTextColor(Color.parseColor("#FFE3E2E2"));
                    statusTextView.setText("acquiring the track");
                    playBeepAndshake(ToneGenerator.TONE_CDMA_DIAL_TONE_LITE, 150);;
                }


            }
        });


        leftImageButton.setOnTouchListener(new View.OnTouchListener() {

            private Handler mHandler;

            @Override public boolean onTouch(View v, MotionEvent event) {
                switch(event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        if (mHandler != null) return true;
                        mHandler = new Handler();
                        String ts = Utils.getCurrentLocalTimeStamp();
                        // -1
                        String msg = getResources().getString(R.string.minus1);
                        new Thread(new MsgSender(settingsAssest.get("serverIP"), settingsAssest.get("serverRxPort"), msg)).start();
                        sentencesDB.parse(msg);

                        playBeepAndshake(ToneGenerator.TONE_CDMA_DIAL_TONE_LITE, 150);;
                        mHandler.postDelayed(mAction, 1000);
                        break;
                    case MotionEvent.ACTION_UP:
                        if (mHandler == null) return true;
                        mHandler.removeCallbacks(mAction);
                        mHandler = null;
                        break;
                }
                return false;
            }

            Runnable mAction = new Runnable() {
                @Override public void run() {
                    String ts = Utils.getCurrentLocalTimeStamp();

                    // -10
                    String msg = getResources().getString(R.string.minus10);
                    new Thread(new MsgSender(settingsAssest.get("serverIP"), settingsAssest.get("serverRxPort"), msg)).start();
                    sentencesDB.parse(msg);

                    playBeepAndshake(ToneGenerator.TONE_CDMA_DIAL_TONE_LITE, 150);;
                    SystemClock.sleep(300);
                    playBeepAndshake(ToneGenerator.TONE_CDMA_DIAL_TONE_LITE, 150);;
                    mHandler.postDelayed(this, 1000);
                }
            };

        });

        rightImageButton.setOnTouchListener(new View.OnTouchListener() {

            private Handler mHandler;

            @Override public boolean onTouch(View v, MotionEvent event) {
                switch(event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        if (mHandler != null) return true;
                        mHandler = new Handler();
                        String ts = Utils.getCurrentLocalTimeStamp();

                        // +1
                        String msg = getResources().getString(R.string.plus1);
                        new Thread(new MsgSender(settingsAssest.get("serverIP"), settingsAssest.get("serverRxPort"), msg)).start();
                        sentencesDB.parse(msg);

                        playBeepAndshake(ToneGenerator.TONE_CDMA_DIAL_TONE_LITE, 150);;

                        mHandler.postDelayed(mAction, 1000);
                        break;
                    case MotionEvent.ACTION_UP:
                        if (mHandler == null) return true;
                        mHandler.removeCallbacks(mAction);
                        mHandler = null;
                        break;
                }
                return false;
            }

            Runnable mAction = new Runnable() {
                @Override public void run() {
                    String ts = Utils.getCurrentLocalTimeStamp();

                    // +10
                    String msg = getResources().getString(R.string.plus10);
                    new Thread(new MsgSender(settingsAssest.get("serverIP"), settingsAssest.get("serverRxPort"), msg)).start();
                    sentencesDB.parse(msg);

                    playBeepAndshake(ToneGenerator.TONE_CDMA_DIAL_TONE_LITE, 150);;
                    SystemClock.sleep(300);
                    playBeepAndshake(ToneGenerator.TONE_CDMA_DIAL_TONE_LITE, 150);;
                    mHandler.postDelayed(this, 1000);
                }
            };

        });

        //new MsgReceiverTask(nmea).execute("");

        new Thread(new MsgReceiver(settingsAssest.get("serverIP"), settingsAssest.get("serverTxPort"), new MsgReceiver.OnMessageReceived() {
            @Override
            //here the messageReceived method is implemented
            public void messageReceived(String message) {
                runOnUiThread(new Runnable() {

                    @Override
                    public void run() {
                        //display.addText("(R) "+message);
                        sentencesDB.parse(message);
                    }
                });
            }
        })).start();


    }
    public void startSettingsActivity(){
        Intent intent = new Intent(this, SettingActivity.class);
        startActivity(intent);
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

}