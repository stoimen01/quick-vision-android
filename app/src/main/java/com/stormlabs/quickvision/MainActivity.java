package com.stormlabs.quickvision;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.util.regex.Pattern;

public class MainActivity extends Activity {

    public static final String IP = "ip";
    public static final String PORT = "port";

    private EditText serverText;
    private EditText portText;

    private static final Pattern PATTERN = Pattern.compile(
            "^(([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\.){3}([01]?\\d\\d?|2[0-4]\\d|25[0-5])$");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        serverText = findViewById(R.id.serverIP);
        portText = findViewById(R.id.serverPort);
    }

    /** Called when the user taps the start button */
    public void startStreaming(View view) {


        String ip = serverText.getText().toString();
        String port = portText.getText().toString();

        if(!validateIP(ip)){
            Toast.makeText(this,"Please enter correct IP address",Toast.LENGTH_SHORT).show();
            return;
        }

        if(!validatePort(port)){
            Toast.makeText(this,"Please enter correct port number",Toast.LENGTH_SHORT).show();
            return;
        }

        Intent videoIntent = new Intent(this, VideoActivity.class);
        videoIntent.putExtra(IP, ip);
        videoIntent.putExtra(PORT, port);

        startActivity(videoIntent);

    }

    public static boolean validateIP(final String ip) {
        return PATTERN.matcher(ip).matches();
    }

    public static boolean validatePort(final String port) {
        Integer portNum = Integer.parseInt(port);
        return portNum > 0 && portNum < 65536 ;
    }
}
