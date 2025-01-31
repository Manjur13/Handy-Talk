package com.example.handytalk;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

public class contact_us extends AppCompatActivity {
ImageView call_manjur,sms_manjur,email_manjur,call_shoaib,sms_shoaib,email_shoaib,sms_meet,call_meet,email_meet,sms_kunal,call_kunal,email_kunal;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_us);
        getSupportActionBar().setTitle("Contact Us");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        call_manjur = findViewById(R.id.call_manjur);
        sms_manjur = findViewById(R.id.sms_manjur);
        email_manjur = findViewById(R.id.email_manjur);

        call_meet = findViewById(R.id.call_meet);
        sms_meet = findViewById(R.id.sms_meet);
        email_meet = findViewById(R.id.email_meet);

        call_shoaib = findViewById(R.id.call_shoaib);
        sms_shoaib = findViewById(R.id.sms_shoaib);
        email_shoaib = findViewById(R.id.email_shoaib);

        call_kunal = findViewById(R.id.call_kunal);
        sms_kunal = findViewById(R.id.sms_kunal);
        email_kunal = findViewById(R.id.email_kunal);

        call_manjur.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String url = "tel:8511313429";
                if (url.startsWith("tel:")) {
                    Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse(url));
                    startActivity(intent); }
            }
        });

        sms_manjur.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Uri uri = Uri.parse("smsto:8511313429");
                Intent intent = new Intent(Intent.ACTION_SENDTO, uri);
                intent.putExtra("sms_body", "Welcome to Handy Talk App");
                startActivity(intent);
            }
        });

        email_manjur.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent emailIntent = new Intent(Intent.ACTION_SENDTO);
                emailIntent.setData(Uri.parse("mailto:kovadiyamanjur@gmail.com"));
                startActivity(Intent.createChooser(emailIntent, "Send feedback"));
            }
        });

        call_meet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String url = "tel:8980450882";
                if (url.startsWith("tel:")) {
                    Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse(url));
                    startActivity(intent); }
            }
        });

        sms_meet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Uri uri = Uri.parse("smsto:8980450882");
                Intent intent = new Intent(Intent.ACTION_SENDTO, uri);
                intent.putExtra("sms_body", "Welcome to Handy Talk App");
                startActivity(intent);
            }
        });

        email_meet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent emailIntent = new Intent(Intent.ACTION_SENDTO);
                emailIntent.setData(Uri.parse("mailto:meetshah1509@gmail.com"));
                startActivity(Intent.createChooser(emailIntent, "Send feedback"));
            }
        });


        call_shoaib.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String url = "tel:9925782220";
                if (url.startsWith("tel:")) {
                    Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse(url));
                    startActivity(intent); }
            }
        });

        sms_manjur.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Uri uri = Uri.parse("smsto:9925782220");
                Intent intent = new Intent(Intent.ACTION_SENDTO, uri);
                intent.putExtra("sms_body", "Welcome to Handy Talk App");
                startActivity(intent);
            }
        });

        email_shoaib.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent emailIntent = new Intent(Intent.ACTION_SENDTO);
                emailIntent.setData(Uri.parse("mailto:mshoaib.ca@gmail.com"));
                startActivity(Intent.createChooser(emailIntent, "Send feedback"));
            }
        });


        call_kunal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String url = "tel:7742384503";
                if (url.startsWith("tel:")) {
                    Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse(url));
                    startActivity(intent); }
            }
        });

        sms_manjur.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Uri uri = Uri.parse("smsto:7742384503");
                Intent intent = new Intent(Intent.ACTION_SENDTO, uri);
                intent.putExtra("sms_body", "Welcome to Handy Talk App");
                startActivity(intent);
            }
        });

        email_manjur.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent emailIntent = new Intent(Intent.ACTION_SENDTO);
                emailIntent.setData(Uri.parse("mailto:"));
                startActivity(Intent.createChooser(emailIntent, "Send feedback"));
            }
        });


    }

    @Override
    public boolean onSupportNavigateUp() {
        Intent in = new Intent(getApplicationContext(),dashboard.class);
        startActivity(in);
        return true;
    }
}