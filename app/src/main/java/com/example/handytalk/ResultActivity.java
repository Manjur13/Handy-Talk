package com.example.handytalk;

import android.Manifest;
import android.app.Dialog;
import android.content.Intent;

import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.QuickContactBadge;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;


import com.airbnb.lottie.Lottie;
import com.airbnb.lottie.LottieAnimationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.mikhaellopez.circularprogressbar.CircularProgressBar;

import org.w3c.dom.Text;


public class ResultActivity extends AppCompatActivity {
    TextView tv, tv2, tv3;
    Button btnRestart;
    Dialog dialog;
    private final static int MY_PERMISSIONS_REQUEST_CAMERA = 12;
    private final static int MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE = 13;
    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;
    LottieAnimationView lottie,congrats;
    private DatabaseReference reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        tv = (TextView)findViewById(R.id.result);
//        tv2 = (TextView)findViewById(R.id.tvres2);
//        tv3 = (TextView)findViewById(R.id.tvres3);
        lottie = findViewById(R.id.lottieresult);
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        btnRestart = (Button) findViewById(R.id.btnRestart);
        congrats = findViewById(R.id.congrats);
getSupportActionBar().setTitle("Result");
        StringBuffer sb = new StringBuffer();
        sb.append("Correct answers: " + QuestionsActivity.correct + "\n");
        StringBuffer sb2 = new StringBuffer();
        sb2.append("Wrong Answers: " + QuestionsActivity.wrong + "\n");
        StringBuffer sb3 = new StringBuffer();
        sb3.append("Final Score: " + QuestionsActivity.correct + "\n");
        tv.setText(QuestionsActivity.correct+"/10");
        reference = FirebaseDatabase.getInstance().getReference("credentials");

        CircularProgressBar circularProgress = findViewById(R.id.circularProgressBar);
        circularProgress.setProgress(QuestionsActivity.correct);
        circularProgress.setRoundBorder(true);
        reference.child(firebaseUser.getUid()).child("marks").setValue(QuestionsActivity.correct);
if(QuestionsActivity.correct>6) {
    congrats.setVisibility(View.VISIBLE);
    lottie.setVisibility(View.VISIBLE);
    btnRestart.setText("Ready to Use Smart Gloves");
    btnRestart.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
// Initialize dialog
            dialog = new Dialog(ResultActivity.this);
// Get the device's screen density
            float density = getResources().getDisplayMetrics().density;

// Set the dialog window size to 400dp x 300dp
            int width = (int) (400 * density);
            int height = (int) (300 * density);

            // set custom dialog
            dialog.setContentView(R.layout.dialogbox_choose);
            dialog.getWindow().setLayout(width, height);

            // set transparent background
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

            // show dialog
            dialog.show();
            // Initialize and assign variable
            ImageView camera = dialog.findViewById(R.id.camera);
            ImageView smartgloves = dialog.findViewById(R.id.smartgloves);
            camera.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (ContextCompat.checkSelfPermission(ResultActivity.this,
                            Manifest.permission.CAMERA)
                            != PackageManager.PERMISSION_GRANTED) {
                        if (!ActivityCompat.shouldShowRequestPermissionRationale(ResultActivity.this,
                                Manifest.permission.CAMERA)) {
                            ActivityCompat.requestPermissions(ResultActivity.this,
                                    new String[]{Manifest.permission.CAMERA},
                                    MY_PERMISSIONS_REQUEST_CAMERA);
                        }
                    }
                    if (ContextCompat.checkSelfPermission(ResultActivity.this,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE)
                            != PackageManager.PERMISSION_GRANTED) {
                        if (!ActivityCompat.shouldShowRequestPermissionRationale(ResultActivity.this,
                                Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                            ActivityCompat.requestPermissions(ResultActivity.this,
                                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                                    MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE);
                        }
                    }
                    Intent in = new Intent(getApplicationContext(), sign_to_text_using_camera.class);
                    startActivity(in);

                }
            });

            smartgloves.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent in = new Intent(getApplicationContext(), signtotext.class);
                    startActivity(in);
                }
            });

        }

    });

}
else
{

    ViewGroup.LayoutParams params = lottie.getLayoutParams();
    params.height = 630;
    congrats.setVisibility(View.GONE);
    lottie.setLayoutParams(params);
    ViewGroup.MarginLayoutParams paramss = (ViewGroup.MarginLayoutParams) lottie.getLayoutParams();
    paramss.topMargin = -20;paramss.bottomMargin = 15;
    lottie.setAnimation(R.raw.tryagain);
    btnRestart.setText("Oops!! Let's Learn");
    btnRestart.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent in = new Intent(getApplicationContext(),learn_sign.class);
            startActivity(in);
        }
    });
}

        QuestionsActivity.correct=0;
        QuestionsActivity.wrong=0;








    }
    @Override
    public boolean onSupportNavigateUp() {
        Intent in = new Intent(getApplicationContext(),ResultActivity.class);
        startActivity(in);
        return true;
    }
}

