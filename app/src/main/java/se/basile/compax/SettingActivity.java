package se.basile.compax;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class SettingActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        SettingsAsset settingsAssest = new SettingsAsset(getBaseContext());

        EditText serverIPeditText = (EditText) findViewById(R.id.serverIPeditText);
        serverIPeditText.setText(settingsAssest.get("serverIP"));

        EditText serverTxPorteditText = (EditText) findViewById(R.id.serverTxPorteditText);
        serverTxPorteditText.setText(settingsAssest.get("serverTxPort"));

        EditText serverRxPorteditText = (EditText) findViewById(R.id.serverRxPorteditText);
        serverRxPorteditText.setText(settingsAssest.get("serverRxPort"));

        Button button = (Button) findViewById(R.id.button);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                settingsAssest.put("serverIP", serverIPeditText.getText().toString());
                settingsAssest.put("serverTxPort", serverTxPorteditText.getText().toString());
                settingsAssest.put("serverRxPort", serverRxPorteditText.getText().toString());

                settingsAssest.writeSettings();
                Toast.makeText(getApplicationContext(),"Restart the app to apply new settings",Toast.LENGTH_SHORT).show();

            }
        });

    }
}