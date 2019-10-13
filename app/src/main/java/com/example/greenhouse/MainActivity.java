package com.example.greenhouse;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;

import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.speech.tts.Voice;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import android.widget.ImageView;
import android.widget.TextView;
import android.widget.VideoView;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.util.HashMap;
import java.util.Locale;


public class MainActivity extends AppCompatActivity implements SensorEventListener {

    private Button scanButton;
    private TextView textViewResult;
    private TextView textViewPoem;
    private VideoView video;
    private SensorManager mySensorService;
    private Boolean isDay;
    private TextToSpeech textToSpeech;
    private Button readButton;
    private Button stopButton;
    private ImageView image;
    private Activity activity = this;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //crear componentes
        mySensorService = (SensorManager) getSystemService(SENSOR_SERVICE);
        image = (ImageView) findViewById(R.id.imageView);
        scanButton = (Button) findViewById(R.id.buttonScan);
        textViewResult = (TextView) findViewById(R.id.textView);
        textViewPoem = (TextView) findViewById(R.id.textView3);
        video = (VideoView) findViewById(R.id.videoView);
        readButton = (Button) findViewById(R.id.buttonRead);
        stopButton = (Button) findViewById(R.id.buttonStop);

        //textToSpeach
        readButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                String data = textViewPoem.getText().toString();
                Voice v = new Voice("en-us-x-sfg#female_1-local", new Locale("en", "US"), 400, 500, true, null);
                textToSpeech.setVoice(v);
                textToSpeech.setSpeechRate((float) 0.80);
                int speechStatus = textToSpeech.speak(data, TextToSpeech.QUEUE_FLUSH, null);
                if (speechStatus == TextToSpeech.ERROR) {
                    Log.e("TTS", "Error	in	converting	Text	to	Speech!");
                }
            }
        });

        stopButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                textToSpeech.stop();
            }
        });

        textToSpeech = new TextToSpeech(getApplicationContext(),
                new TextToSpeech.OnInitListener() {
                    @Override
                    public void onInit(int status) {
                        if (status == TextToSpeech.SUCCESS) {
                            textToSpeech.setLanguage(Locale.US);
                        }
                    }
                });

        //Scanner de qr codes
        scanButton.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {
                IntentIntegrator integrator = new IntentIntegrator(activity);
                integrator.setDesiredBarcodeFormats(IntentIntegrator.QR_CODE_TYPES);
                integrator.setPrompt("Scan");
                integrator.setCameraId(0);
                integrator.setBeepEnabled(false);
                integrator.setBarcodeImageEnabled(false);
                integrator.initiateScan();
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, intent);
        super.onActivityResult(requestCode, resultCode, intent);
        if (result != null) {
            if (result.getContents() != null) {
                handleScan(result.getContents());

            }
            else if (result.getContents() == null) {
            }
        }
    }

    private void handleScan(String contents) {
        int raw = matchVideo(contents);
        String explain = matchExplanation(contents);
        if (raw > 0) {
            textViewResult.setText("Listening to the local wildlife around " + contents);
            textViewResult.setTextColor(Color.BLACK);
            textViewPoem.setText(explain);
            readButton.setVisibility(View.VISIBLE);
            stopButton.setVisibility(View.VISIBLE);
            image.setVisibility(View.INVISIBLE);
            Uri uri = Uri.parse("android.resource://com.example.greenhouse/" + raw);
            video.setVisibility(View.VISIBLE);
            video.setVideoURI(uri);
            video.requestFocus();
            video.start();
        }
    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        if (sensorEvent.sensor.getType() == Sensor.TYPE_LIGHT) {
            if (sensorEvent.values[0] > 20)
                isDay = true;
            else
                isDay = false;
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }

    @Override
    protected void onResume() {
        super.onResume();
        mySensorService.registerListener(this, mySensorService.getDefaultSensor(
                Sensor.TYPE_LIGHT), mySensorService.SENSOR_DELAY_NORMAL);
    }

    @Override
    protected void onPause() {
        super.onPause();
        mySensorService.unregisterListener(this);
        textToSpeech.stop();
    }


    private int matchVideo(String contents) {
        if (isDay) {
            switch (contents) {

                case "bellis perennis":
                    return R.raw.daisyday;
                case "acacia tortilis":
                    return R.raw.acaciaday;
                case "pine tree":
                    return R.raw.pinecornday;
                default:
                    return 0;

            }
        } else {
            switch (contents) {

                case "bellis perennis":
                    return R.raw.daisynight;
                case "acacia tortilis":
                    return R.raw.acacianight;
                case "pine tree":
                    return R.raw.pinecornnight;
                default:
                    return 0;

            }
        }


    }

    private String matchExplanation(String contents) {

        if (isDay) {
            switch (contents) {

                case "bellis perennis":
                    return  getString(R.string.daisyday);
                case "acacia tortilis":
                    return  getString(R.string.acaciaday);
                case "pine tree":
                    return  getString(R.string.pineday);
                default:
                    return "";

            }
        } else {
            switch (contents) {

                case "bellis perennis":
                    return  getString(R.string.daisynight);
                case "acacia tortilis":
                    return  getString(R.string.acacianight);
                case "pine tree":
                    return  getString(R.string.pinenight);
                default:
                    return "";

            }
        }


    }
}