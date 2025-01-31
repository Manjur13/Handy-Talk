package com.example.handytalk;


import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraManager;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import org.opencv.android.BaseLoaderCallback;
import org.opencv.android.CameraBridgeViewBase;
import org.opencv.android.JavaCameraView;
import org.opencv.android.OpenCVLoader;
import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.core.Scalar;
import org.opencv.imgproc.Imgproc;

import java.io.IOException;
import java.util.Locale;

public class sign_to_text_using_camera extends AppCompatActivity implements CameraBridgeViewBase.CvCameraViewListener2 {


    private static final int CLASSIFY_INTERVAL = 20;
    private static final String TAG = "RecognitionActivity";
    TextToSpeech textToSpeech;
    Button clrbtn;
    private Classifier classifier;
    private Mat frame;
    private Mat mRGBA;
    private JavaCameraView openCvCameraView;
    private CameraManager cameraManager;
    private String cameraId;
    private ImageView flashlightbtn, ttlbtncamera;
    private TextView signreading;
    private final Boolean isDebug = false;
    private final Boolean isEdge = false;
    private final Boolean isSave = false;
    private String text = "";
    private int counter = 0;

    private final BaseLoaderCallback baseloadercallback = new BaseLoaderCallback(this) {
        @Override
        public void onManagerConnected(int status) {
            super.onManagerConnected(status);
            if (status == BaseLoaderCallback.SUCCESS)
                openCvCameraView.enableView();
            else
                super.onManagerConnected(status);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_sign_to_text_using_camera);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Sign to Text Using Camera");
        signreading = (TextView) findViewById(R.id.signreading);
        ttlbtncamera = findViewById(R.id.texttospeechcamera);
        openCvCameraView = findViewById(R.id.camera_view);
        openCvCameraView.setCvCameraViewListener(this);
        openCvCameraView.enableView();
        clrbtn = findViewById(R.id.clrbtn);
        flashlightbtn = findViewById(R.id.flashlight);
        cameraManager = (CameraManager) getSystemService(Context.CAMERA_SERVICE);
        try {
            cameraId = cameraManager.getCameraIdList()[0];
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
        flashlightbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ClipboardManager clipboardManager = (ClipboardManager)
                        getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clipData = ClipData.newPlainText("nonsense_data",
                        signreading.getText().toString());
                clipboardManager.setPrimaryClip(clipData);
                Toast.makeText(getApplicationContext(), "COPIED YOUR TEXT", Toast.LENGTH_SHORT).show();
            }
        });
        // create an object textToSpeech and adding features into it
        textToSpeech = new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int i) {

                // if No error is found then only it will run
                if (i != TextToSpeech.ERROR) {
                    // To Choose language of speech
                    textToSpeech.setLanguage(Locale.UK);
                }
            }
        });
        ttlbtncamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                textToSpeech.speak(signreading.getText().toString(), TextToSpeech.QUEUE_FLUSH, null);
            }
        });

        clrbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                text = "";
            }
        });

    }

    @Override
    public void onResume() {
        super.onResume();

        if (OpenCVLoader.initDebug()) {
            Log.d(TAG, "Connected camera.");
            baseloadercallback.onManagerConnected(BaseLoaderCallback.SUCCESS);
        } else {
            Log.d(TAG, "Camera not connected.");
            OpenCVLoader.initAsync(OpenCVLoader.OPENCV_VERSION, this, baseloadercallback);
        }


        try {
            classifier = new Classifier(this);
        } catch (IOException e) {
            Log.e(TAG, "Failed to initialize classifier", e);
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (openCvCameraView != null) {
            openCvCameraView.disableView();
        }
        if (classifier != null) {
            classifier.close();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        if (openCvCameraView != null) {
            openCvCameraView.disableView();
        }
    }

    @Override
    public void onCameraViewStarted(int width, int height) {
        mRGBA = new Mat();
    }

    @Override
    public void onCameraViewStopped() {
        if (mRGBA != null) mRGBA.release();
    }

    @Override
    public Mat onCameraFrame(CameraBridgeViewBase.CvCameraViewFrame inputFrame) {
        float mh = mRGBA.height();
        float cw = (float) Resources.getSystem().getDisplayMetrics().widthPixels;
        float scale = mh / cw * 0.7f;

        mRGBA = inputFrame.rgba();
        frame = classifier.processMat(mRGBA);

        if (!isDebug) {
            if (counter == CLASSIFY_INTERVAL) {
                runInterpreter();
                counter = 0;
            } else {
                counter++;
            }
        }

        Imgproc.rectangle(mRGBA,
                new Point(mRGBA.cols() / 2f - (mRGBA.cols() * scale / 2),
                        mRGBA.rows() / 2f - (mRGBA.cols() * scale / 2)),
                new Point(mRGBA.cols() / 2f + (mRGBA.cols() * scale / 2),
                        mRGBA.rows() / 2f + (mRGBA.cols() * scale / 2)),
                new Scalar(0, 255, 0), 1);
        if (isEdge) {
            mRGBA = classifier.debugMat(mRGBA);
        }

        System.gc();
        return mRGBA;
    }

    private void runInterpreter() {
        if (classifier != null) {
            classifier.classifyMat(frame);
            switch (classifier.getResult()) {
                case "SPACE":
                    text += " ";
                    break;
                case "BACKSPACE":
                    text = text.substring(0, text.length() - 1);
                    break;
                case "NOTHING":
                    break;
                default:
                    text += classifier.getResult();
            }
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    signreading.setText(text);
                }
            });
            Log.d(TAG, "Guess: " + classifier.getResult() + " Probability: " + classifier.getProbability());
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        Intent in = new Intent(getApplicationContext(), dashboard.class);
        startActivity(in);
        return true;
    }

}