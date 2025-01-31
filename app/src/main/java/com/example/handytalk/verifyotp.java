package com.example.handytalk;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.TaskExecutors;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

public class verifyotp extends AppCompatActivity {

    private String verificationId;
    private FirebaseAuth mAuth;
    ProgressBar progressBar;
    EditText inputcode1, inputcode2, inputcode3, inputcode4, inputcode5, inputcode6, inputcode7;
    Button buttonSignIn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verifyotp);

        mAuth = FirebaseAuth.getInstance();
        progressBar = findViewById(R.id.progressbar);
        getSupportActionBar().hide();
        inputcode1 = findViewById(R.id.inputcode1);
        inputcode2 = findViewById(R.id.inputcode2);
        inputcode3 = findViewById(R.id.inputcode3);
        inputcode4 = findViewById(R.id.inputcode4);
        inputcode5 = findViewById(R.id.inputcode5);
        inputcode6 = findViewById(R.id.inputcode6);
        TextView mobilenumber = (TextView)findViewById(R.id.textmobile);
        TextView timer = (TextView)findViewById(R.id.timer);
        buttonSignIn = findViewById(R.id.buttonContinue);
        String phoneNumber = getIntent().getStringExtra("phoneNumber");
        mobilenumber.setText(phoneNumber);
        sendVerificationCode(phoneNumber);
        inputcode1.addTextChangedListener(new GenericTextWatcher(inputcode1));
        inputcode2.addTextChangedListener(new GenericTextWatcher(inputcode2));
        inputcode3.addTextChangedListener(new GenericTextWatcher(inputcode3));
        inputcode4.addTextChangedListener(new GenericTextWatcher(inputcode4));
        inputcode5.addTextChangedListener(new GenericTextWatcher(inputcode5));
        inputcode6.addTextChangedListener(new GenericTextWatcher(inputcode6));
            new CountDownTimer(60000, 1000) {

                public void onTick(long millisUntilFinished) {
                    int seconds = (int) (millisUntilFinished / 1000);
                    int minutes = seconds / 60;
                    seconds = seconds % 60;
                    timer.setText("" + String.format("%02d", minutes)
                            + ":" + String.format("%02d", seconds));
                }

                public void onFinish() {
                    timer.setText("RESEND IT");
                    timer.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            sendVerificationCode(phoneNumber);
                        }
                    });

                }
            }.start();

//        // save phone number
//        SharedPreferences prefs = getApplicationContext().getSharedPreferences("USER_PREF",
//                Context.MODE_PRIVATE);
//        SharedPreferences.Editor editor = prefs.edit();
//        editor.putString("phoneNumber", phoneNumber);
//        editor.apply();

        buttonSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if (inputcode1.getText().toString().trim().isEmpty()
                        || inputcode2.getText().toString().trim().isEmpty()
                        || inputcode3.getText().toString().trim().isEmpty()
                        || inputcode4.getText().toString().trim().isEmpty()
                        || inputcode5.getText().toString().trim().isEmpty()
                        || inputcode6.getText().toString().trim().isEmpty()) {

                    Toast.makeText(verifyotp.this, "PLEASE ENTER VALID CODE", Toast.LENGTH_SHORT).show();
                    return;
                } else {

                    String code = inputcode1.getText().toString() + inputcode2.getText().toString() + inputcode3.getText().toString() + inputcode4.getText().toString() + inputcode5.getText().toString() + inputcode6.getText().toString();
                    verifyCode(code);

                }
            }
        });

    }

    private void verifyCode(String code) {
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationId, code);
        signInWithCredential(credential);
    }

    private void signInWithCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {

                            Intent intent = new Intent(verifyotp.this, dashboard.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

                            startActivity(intent);

                        } else {
                            Toast.makeText(verifyotp.this, task.getException().getMessage(), Toast.LENGTH_LONG).show();
                            progressBar.setVisibility(View.GONE);
                        }
                    }
                });
    }

    private void sendVerificationCode(String number) {
        progressBar.setVisibility(View.VISIBLE);
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                number,
                60,
                TimeUnit.SECONDS,
                this,
                mCallBack
        );

        progressBar.setVisibility(View.GONE);
    }

    private final PhoneAuthProvider.OnVerificationStateChangedCallbacks
            mCallBack = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

        @Override
        public void onCodeSent(String s, PhoneAuthProvider.ForceResendingToken forceResendingToken) {
            super.onCodeSent(s, forceResendingToken);
            verificationId = s;
        }

        @Override
        public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {
            String code = phoneAuthCredential.getSmsCode();
            if (code != null) {
                inputcode1.setText(code);
                inputcode2.setText(code);
                inputcode3.setText(code);
                inputcode4.setText(code);
                inputcode5.setText(code);
                inputcode6.setText(code);


                verifyCode(code);
            }
        }

        @Override
        public void onVerificationFailed(FirebaseException e) {
            Toast.makeText(verifyotp.this, e.getMessage(), Toast.LENGTH_LONG).show();
            progressBar.setVisibility(View.GONE);
        }
    };
    public class GenericTextWatcher implements TextWatcher
    {
        private final View view;
        private GenericTextWatcher(View view)
        {
            this.view = view;
        }

        @Override
        public void afterTextChanged(Editable editable) {
            // TODO Auto-generated method stub
            String text = editable.toString();
            switch(view.getId())
            {

                case R.id.inputcode1:
                    if(text.length()==1)
                        inputcode2.requestFocus();
                    break;
                case R.id.inputcode2:
                    if(text.length()==1)
                        inputcode3.requestFocus();
                    else if(text.length()==0)
                        inputcode1.requestFocus();
                    break;
                case R.id.inputcode3:
                    if(text.length()==1)
                        inputcode4.requestFocus();
                    else if(text.length()==0)
                        inputcode2.requestFocus();
                    break;
                case R.id.inputcode4:
                    if(text.length()==1)
                        inputcode5.requestFocus();
                    else if(text.length()==0)
                        inputcode3.requestFocus();
                    break;

                case R.id.inputcode5:
                    if(text.length()==1)
                        inputcode6.requestFocus();
                    else if(text.length()==0)
                        inputcode4.requestFocus();
                    break;
                case R.id.inputcode6:
                    if(text.length()==0)
                        inputcode5.requestFocus();
                    break;
            }
        }

        @Override
        public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
            // TODO Auto-generated method stub
        }

        @Override
        public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
            // TODO Auto-generated method stub
        }
    }

}

