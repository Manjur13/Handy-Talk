package com.example.handytalk;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.GridView;

import com.example.handytalk.model.Learnsign_model;

import java.util.ArrayList;

public class learn_sign extends AppCompatActivity {

    GridView coursesGV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_learn_sign);
        getSupportActionBar().setTitle("Learn Sign");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        coursesGV = findViewById(R.id.idGVcourses);
        ArrayList<Learnsign_model> courseModelArrayList = new ArrayList<Learnsign_model>();

        courseModelArrayList.add(new Learnsign_model("O", R.drawable.one));
        courseModelArrayList.add(new Learnsign_model("V", R.drawable.two));
        courseModelArrayList.add(new Learnsign_model("S", R.drawable.three));
        courseModelArrayList.add(new Learnsign_model("I", R.drawable.four));
        courseModelArrayList.add(new Learnsign_model("Y", R.drawable.eight));
        courseModelArrayList.add(new Learnsign_model("C", R.drawable.six));
        courseModelArrayList.add(new Learnsign_model("O", R.drawable.one));
        courseModelArrayList.add(new Learnsign_model("V", R.drawable.two));
        courseModelArrayList.add(new Learnsign_model("S", R.drawable.three));
        courseModelArrayList.add(new Learnsign_model("I", R.drawable.four));
        courseModelArrayList.add(new Learnsign_model("Y", R.drawable.eight));
        courseModelArrayList.add(new Learnsign_model("C", R.drawable.six));
        courseModelArrayList.add(new Learnsign_model("O", R.drawable.one));
        courseModelArrayList.add(new Learnsign_model("V", R.drawable.two));
        courseModelArrayList.add(new Learnsign_model("S", R.drawable.three));
        courseModelArrayList.add(new Learnsign_model("I", R.drawable.four));
        courseModelArrayList.add(new Learnsign_model("Y", R.drawable.eight));
        courseModelArrayList.add(new Learnsign_model("C", R.drawable.six));

        learnsign_adapter adapter = new learnsign_adapter(this, courseModelArrayList);
        coursesGV.setAdapter(adapter);

    }
    @Override
    public boolean onSupportNavigateUp() {
        Intent in = new Intent(getApplicationContext(),dashboard.class);
        startActivity(in);
        return true;
    }
}