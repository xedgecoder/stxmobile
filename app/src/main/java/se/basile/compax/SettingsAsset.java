package se.basile.compax;


import android.content.Context;
import android.os.Environment;
import android.text.TextUtils;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.MissingResourceException;

import se.basile.compax.nmea.SentenceAbstr;

public class SettingsAsset {
    private BufferedReader bfr;
    Context context;
    private Map<String, String> settings = new HashMap<String, String>();

    public SettingsAsset(Context context)  {
        this.context = context;
        FileInputStream fis = null;
        File file = new File(context.getFilesDir(), "config.json");

        String config = "";
        if(file.exists()) {
            // use the config.
            try {
                fis = new FileInputStream(context.getFilesDir()+"/config.json");
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            InputStreamReader inputreader = new InputStreamReader(fis);
            bfr = new BufferedReader(inputreader);

        } else {
            InputStream stream = context.getResources().openRawResource(R.raw.config);
            //java.io.InputStream stream = context.getAssets().open("config.json");
            bfr = new BufferedReader(new InputStreamReader(stream));
        }
        readSettings();

    }

    public String get(String key) {
        return settings.get(key);
    }

    public String put(String key, String value) {
        settings.put(key, value);
        return value;
    }

    public void writeSettings() {
        JSONArray ja = new JSONArray();

        for ( Map.Entry<String, String> entry  : settings.entrySet()) {

            try {
                JSONObject jo = new JSONObject();

                jo.put("key", entry.getKey());
                jo.put("value", entry.getValue());
                ja.put(jo);

            } catch (JSONException e) {
                e.printStackTrace();
            }

        }

        JSONObject mainObj = new JSONObject();
        try {
            mainObj.put("server_settings", ja);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        
        FileOutputStream outputStream;

        try {
            outputStream = context.openFileOutput("config.json", Context.MODE_PRIVATE);
            outputStream.write(mainObj.toString().getBytes());
            outputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    private void readSettings() {
        String line;
        StringBuilder text = new StringBuilder();

        // read config.json and copý to String
        try {
            while ((line = bfr.readLine()) != null) {
                text.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
            return ;
        }
        String jsonStr = text.toString();
        // From String to JSON and then to Map items
        if (TextUtils.isEmpty(jsonStr)) {

        }
        else{
            try {
                this.parseJsonFromString(jsonStr);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }


    private void parseJsonFromString(String jsonString){
        try {
            JSONObject contentJson = new JSONObject(jsonString);
            Log.d("COMPAX", contentJson.toString());

            JSONArray settingsArray = contentJson.getJSONArray("server_settings");

            for (int i = 0; i < settingsArray.length(); i++) {
                String key = "", value = "";

                JSONObject itemDetails = settingsArray.getJSONObject(i);

                if (!itemDetails.isNull("key")) {
                    key = itemDetails.getString("key");
                }
                if (!itemDetails.isNull("value")) {
                    value = itemDetails.getString("value");
                }
                put(key, value);

            }

        } catch (JSONException e) {
            e.printStackTrace();
            return;
        }

        return;
    }


}
