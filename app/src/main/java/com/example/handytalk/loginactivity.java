package com.example.handytalk;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.handytalk.model.Session_manager;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;

import br.com.simplepass.loadingbutton.customViews.CircularProgressButton;

public class loginactivity extends AppCompatActivity {
    TextView Buttonregister, forgetpass;
    TextInputLayout textInputLayoutForEmail, textInputLayoutForpassword;
    TextInputEditText loginemail, loginpassword;
    FirebaseAuth mAuth;
    CircularProgressButton logginbtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loginactivity);
        getSupportActionBar().hide();
        Buttonregister = findViewById(R.id.button_register);

        loginemail = findViewById(R.id.loginemail);
        loginpassword = findViewById(R.id.loginpassword);
        logginbtn = (CircularProgressButton) findViewById(R.id.logginbtn);

        forgetpass = findViewById(R.id.button_reset);


        textInputLayoutForEmail = findViewById(R.id.loginTextFieldEMAIL);
        textInputLayoutForpassword = findViewById(R.id.loginTextFieldPassword);


        Buttonregister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(loginactivity.this, registrationactivity.class);
                startActivity(intent);
            }
        });

        logginbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                loginwork();


            }
        });
        forgetpass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

//                Intent intent = new Intent(loginactivity.this, forget_password.class);
//                startActivity(intent);

            }
        });

    }
    private void loginwork() {

        // Check if email is empty
        if (loginemail.getText().toString().isEmpty()) {
            textInputLayoutForEmail.setError("Enter Email Address");

        }

        // Check if password is empty
        if (loginpassword.getText().toString().isEmpty()) {
            textInputLayoutForpassword.setError("Enter Password");
            loginemail.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                    textInputLayoutForEmail.setError(null);
                    textInputLayoutForEmail.setErrorEnabled(false);

                }

                @Override
                public void afterTextChanged(Editable editable) {
                    textInputLayoutForEmail.setError(null);
                    textInputLayoutForEmail.setErrorEnabled(false);
                }

            });
        }
                if (loginpassword.getText().toString().isEmpty()) {
            textInputLayoutForpassword.setError("Enter Password");
            loginpassword.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                    textInputLayoutForpassword.setError(null);
                    textInputLayoutForpassword.setErrorEnabled(false);


                }

                @Override
                public void afterTextChanged(Editable editable) {
                    textInputLayoutForpassword.setError(null);
                    textInputLayoutForpassword.setErrorEnabled(false);

                }

            });
        }
        else {
            // Start loading animation
            logginbtn.startAnimation();

            // Sign in with email and password
            FirebaseAuth.getInstance().signInWithEmailAndPassword(loginemail.getText().toString(), loginpassword.getText().toString())
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            // Stop loading animation on failure
                            logginbtn.revertAnimation();
                            Toast.makeText(loginactivity.this, "Try Again Email Id or Password Wrong", Toast.LENGTH_SHORT).show();
                        }
                    }).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                @Override
                public void onSuccess(AuthResult authResult) {
                    checkIfEmailVerified();
                    // Stop loading animation on success
                    logginbtn.doneLoadingAnimation(getResources().getColor(R.color.purple_700), BitmapFactory.decodeResource(getResources(), br.com.simplepass.loadingbutton.R.drawable.ic_done_white_48dp));
                }
            });

        }
    }


//    private void loginwork() {
//
//        if (loginemail.getText().toString().isEmpty()) {
//            textInputLayoutForEmail.setError("Enter Email Address");
//            loginemail.addTextChangedListener(new TextWatcher() {
//                @Override
//                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//
//                }
//
//                @Override
//                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//                    textInputLayoutForEmail.setError(null);
//                    textInputLayoutForEmail.setErrorEnabled(false);
//
//                }
//
//                @Override
//                public void afterTextChanged(Editable editable) {
//                    textInputLayoutForEmail.setError(null);
//                    textInputLayoutForEmail.setErrorEnabled(false);
//                }
//
//            });
//        }
//
//        if (loginpassword.getText().toString().isEmpty()) {
//            textInputLayoutForpassword.setError("Enter Password");
//            loginpassword.addTextChangedListener(new TextWatcher() {
//                @Override
//                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//
//                }
//
//                @Override
//                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//                    textInputLayoutForpassword.setError(null);
//                    textInputLayoutForpassword.setErrorEnabled(false);
//
//
//                }
//
//                @Override
//                public void afterTextChanged(Editable editable) {
//                    textInputLayoutForpassword.setError(null);
//                    textInputLayoutForpassword.setErrorEnabled(false);
//
//                }
//
//            });
//        } else {
//
//            FirebaseAuth.getInstance().signInWithEmailAndPassword(loginemail.getText().toString(), loginpassword.getText().toString())
//                    .addOnFailureListener(new OnFailureListener() {
//                        @Override
//                        public void onFailure(@NonNull Exception e) {
//                            Toast.makeText(loginactivity.this, "Try Again Email Id or Password Wrong", Toast.LENGTH_SHORT).show();
//
//
//                        }
//                    }).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
//                @Override
//                public void onSuccess(AuthResult authResult) {
//                    checkIfEmailVerified();
////                    sendUserToNextActivity();
//
//                }
//            });
//
//        }
//    }

    private void checkIfEmailVerified() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        if (user.isEmailVerified()) {
            // user is verified, so you can finish this activity or send user to activity which you want.
            sendUserToNextActivity();

            Toast.makeText(loginactivity.this, "Login Succesfull: " + user.getEmail(), Toast.LENGTH_SHORT).show();
        } else {
            // email is not verified, so just prompt the message to the user and restart this activity.
            // NOTE: don't forget to log out the user.
            FirebaseAuth.getInstance().signOut();

            //restart this activity

        }
    }

    private void sendUserToNextActivity() {
        Intent intent = new Intent(getApplicationContext(), dashboard.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }


}
