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
    public static final String SIZE = "size";
    public static final String QUALITY = "quality";

    private EditText serverIpText;
    private EditText serverPortText;
    private EditText videoSizeText;
    private EditText videoQualityText;

    private static final Pattern PATTERN = Pattern.compile(
            "^(([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\.){3}([01]?\\d\\d?|2[0-4]\\d|25[0-5])$");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        serverIpText = findViewById(R.id.server_ip);
        serverPortText = findViewById(R.id.server_port);
        videoQualityText = findViewById(R.id.video_quality);
        videoSizeText = findViewById(R.id.video_size);

    }

    /** Called when the user taps the start button */
    public void startStreaming(View view) {

        String ip = serverIpText.getText().toString();
        String port = serverPortText.getText().toString();
        String size = videoSizeText.getText().toString();
        String quality = videoQualityText.getText().toString();

        if(!validateIP(ip)){
            Toast.makeText(this,"Please enter correct IP address",Toast.LENGTH_SHORT).show();
            return;
        }

        if(!validatePort(port)){
            Toast.makeText(this,"Please enter correct port number",Toast.LENGTH_SHORT).show();
            return;
        }

        if(!validateSize(size)){
            Toast.makeText(this,"Please enter size below or 512",Toast.LENGTH_SHORT).show();
            return;
        }

        if(!validateQuality(quality)){
            Toast.makeText(this,"Please enter quality between 0 and 100",Toast.LENGTH_SHORT).show();
            return;
        }

        Intent videoIntent = new Intent(this, VideoActivity.class);
        videoIntent.putExtra(IP, ip);
        videoIntent.putExtra(PORT, port);
        videoIntent.putExtra(SIZE, size);
        videoIntent.putExtra(QUALITY, quality);

        startActivity(videoIntent);
    }

    public static boolean validateIP(final String ip) {
        return PATTERN.matcher(ip).matches();
    }

    public static boolean validatePort(final String port) {
        Integer portNum = Integer.parseInt(port);
        return portNum > 0 && portNum < 65536 ;
    }

    public static boolean validateSize(final String s) {
        Integer size = Integer.parseInt(s);
        return size > 0 && size <= 512 ;
    }

    public static boolean validateQuality(final String q) {
        Integer quality = Integer.parseInt(q);
        return quality > 0 && quality <= 100 ;
    }
}
