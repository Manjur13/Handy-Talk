package com.example.handytalk;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;

import com.example.handytalk.model.Session_manager;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class getstarted extends AppCompatActivity {
Button loginbtn;
Button otpbtn;
FirebaseAuth mAuth;
Session_manager sessionManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_getstarted);
        getSupportActionBar().hide();
        mAuth = FirebaseAuth.getInstance();
        loginbtn = findViewById(R.id.button);
        otpbtn = findViewById(R.id.button1);
        FirebaseAuth auth = FirebaseAuth.getInstance();
        if (auth.getCurrentUser() != null) {
            Intent intent = new Intent(getstarted.this,dashboard.class);
            startActivity(intent);
            finish();
        }
        else
        {
            loginbtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(getstarted.this,loginactivity.class);
                    startActivity(intent);
                }
            });
            otpbtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(getstarted.this,otp_screen.class);
                    startActivity(intent);
                }
            });
        }






    }


}