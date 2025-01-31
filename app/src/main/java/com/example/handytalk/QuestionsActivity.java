package com.example.handytalk;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;

public class QuestionsActivity extends AppCompatActivity {
    ImageView tv;
    Button submitbutton, quitbutton;
    RadioGroup radio_g;
    RadioButton rb1,rb2,rb3,rb4;
    private ProgressBar mProgressBar;
    private CountDownTimer mCountDownTimer;
    private int mProgressValue = 0;
//    String questions[] = {
//                            "Which method can be defined only once in a program?",
//                            "Which of these is not a bitwise operator?",
//                            "Which keyword is used by method to refer to the object that invoked it?",
//                            "Which of these keywords is used to define interfaces in Java?",
//                            "Which of these access specifiers can be used for an interface?",
//                            "Which of the following is correct way of importing an entire package ‘pkg’?",
//                            "What is the return type of Constructors?",
//                            "Which of the following package stores all the standard java classes?",
//                            "Which of these method of class String is used to compare two String objects for their equality?",
//                            "An expression involving byte, int, & literal numbers is promoted to which of these?"
//                         };
ArrayList<Integer> questions = new ArrayList<Integer>();


    String[] answers = {"A","2","import","Interface","public","Import pkg.","int","Are you pulling my leg?","equalss()","int"};
    String[] opt = {
                    "A","0","B","O",
                    "2","0","B","V",
                    "import","this","catch","abstract",
                    "Interface","interface","intf","Intf",
                    "public","protected","private","All of the mentioned",
                    "Import pkg.","import pkg.*","Import pkg.*","import pkg.",
                    "int","float","void","None of the mentioned",
                    "Are you pulling my leg?","Where are you?","I am thirsty","Nature's Call",
                    "equalss()","Equals()","isequal()","Isequal()",
                     "int","long","byte","float"
                   };
    int flag=0;
    public static int marks=0,correct=0,wrong=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_questions);

        final TextView score = (TextView)findViewById(R.id.textView4);
        getSupportActionBar().setTitle("Quiz");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        questions.add(R.drawable.one);
        questions.add(R.drawable.two);
        questions.add(R.drawable.three);
        questions.add(R.drawable.four);
        questions.add(R.drawable.five);
        questions.add(R.drawable.six);
        questions.add(R.drawable.seven);
        questions.add(R.drawable.eight);
        questions.add(R.drawable.nine);
        questions.add(R.drawable.ten);

//        String name= intent.getStringExtra("myname");
//
//        if (name.trim().equals(""))
//            textView.setText("Hello User");
//        else
//        textView.setText("Hello " + name);

        submitbutton=(Button)findViewById(R.id.button3);
        quitbutton=(Button)findViewById(R.id.buttonquit);
        tv=findViewById(R.id.tvque);
        mProgressBar = findViewById(R.id.progress_bar);
        radio_g=(RadioGroup)findViewById(R.id.answersgrp);
        rb1=(RadioButton)findViewById(R.id.radioButton);
        rb2=(RadioButton)findViewById(R.id.radioButton2);
        rb3=(RadioButton)findViewById(R.id.radioButton3);
        rb4=(RadioButton)findViewById(R.id.radioButton4);
//        tv.setText(questions[flag]);
        tv.setImageResource(questions.get(flag));
        rb1.setText(opt[0]);
        rb2.setText(opt[1]);
        rb3.setText(opt[2]);
        rb4.setText(opt[3]);
        submitbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //int color = mBackgroundColor.getColor();
                //mLayout.setBackgroundColor(color);
                if(radio_g.getCheckedRadioButtonId()==-1)
                {
                    Toast.makeText(getApplicationContext(), "Please select one choice", Toast.LENGTH_SHORT).show();
                    return;
                }
                RadioButton uans = (RadioButton) findViewById(radio_g.getCheckedRadioButtonId());
                String ansText = uans.getText().toString();
//                Toast.makeText(getApplicationContext(), ansText, Toast.LENGTH_SHORT).show();
                if(ansText.equals(answers[flag])) {
                    correct++;
                    Toast.makeText(getApplicationContext(), "Correct", Toast.LENGTH_SHORT).show();

                }
                else {
                    wrong++;
                    Toast.makeText(getApplicationContext(), "Wrong", Toast.LENGTH_SHORT).show();
                }

                flag++;

                if (score != null)
//                    score.setText(""+correct);

                if(flag<=9)
                {

                    tv.setImageResource(questions.get(flag));
//                    tv.setText(questions.[flag]);
                    rb1.setText(opt[flag*4]);
                    rb2.setText(opt[flag*4 +1]);
                    rb3.setText(opt[flag*4 +2]);
                    rb4.setText(opt[flag*4 +3]);

                }
                else
                {
                    marks=correct;
                    Intent in = new Intent(getApplicationContext(),ResultActivity.class);
                    startActivity(in);
                }
                radio_g.clearCheck();
            }
        });

        quitbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(QuestionsActivity.this)
                        .setTitle("Submit Quiz")
                        .setMessage("Are you sure you want to submit?")
                        .setIcon(R.drawable.logot)

                        // Specifying a listener allows you to take an action before dismissing the dialog.
                        // The dialog is automatically dismissed when a dialog button is clicked.
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                Intent intent=new Intent(getApplicationContext(),ResultActivity.class);
                                startActivity(intent);
                            }
                        })

                        // A null listener allows the button to dismiss the dialog and take no further action.
                        .setNegativeButton(android.R.string.no, null)
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .show();

            }
        });

        mCountDownTimer = new CountDownTimer(100000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                mProgressValue++;
                mProgressBar.setProgress(mProgressValue);
            }

            @Override
            public void onFinish() {
                Toast.makeText(getApplicationContext(),"Time's Up",Toast.LENGTH_SHORT).show();
                Intent in = new Intent(getApplicationContext(),ResultActivity.class);
                startActivity(in);
            }
        };
        mCountDownTimer.start();

    }
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        mCountDownTimer.cancel();
        return true;
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onPause() {

        super.onPause();
        mCountDownTimer.cancel();
    }

    @Override
    protected void onDestroy() {
        mCountDownTimer.cancel();
        super.onDestroy();
    }
}