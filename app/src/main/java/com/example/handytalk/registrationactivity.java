package com.example.handytalk;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.handytalk.model.Users;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import nu.aaro.gustav.passwordstrengthmeter.PasswordStrengthLevel;
import nu.aaro.gustav.passwordstrengthmeter.PasswordStrengthMeter;

public class registrationactivity extends AppCompatActivity {
    ProgressDialog progressDialog;
    String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
    private FirebaseAuth mAuth;
    private DatabaseReference reference;
    TextInputLayout textInputLayoutForUsername,textInputLayoutForEmail,textInputLayoutForpassword,textInputLayoutforconfirmpassword;
    TextInputEditText Username,signupemail,signuppassword,confirmpassword;
    MaterialButton signupbtn;
    TextView btn_logingoes;

    ImageView backbtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registrationactivity);
        getSupportActionBar().hide();
        textInputLayoutForUsername = findViewById(R.id.outlinedTextLayoutusername);
        textInputLayoutForEmail = findViewById(R.id.outlinedTextFieldEmail);
        textInputLayoutForpassword = findViewById(R.id.outlinedTextFieldPassword);
        progressDialog = new ProgressDialog(registrationactivity.this,R.style.AppCompatAlertDialogStyle);
        textInputLayoutforconfirmpassword = findViewById(R.id.outlinedTextFieldPasswordconfirm);
        Username = findViewById(R.id.username);
        backbtn = findViewById(R.id.backpressbtn);
        signupemail = findViewById(R.id.signupemail);
        signuppassword = findViewById(R.id.signuppassword);
        PasswordStrengthMeter meter1 = findViewById(R.id.passwordInputMeter);
        meter1.setEditText(signuppassword);
        meter1.setVisibility(View.GONE);
        signuppassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                meter1.setVisibility(View.GONE);
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                meter1.setVisibility(View.GONE);


            }

            @Override
            public void afterTextChanged(Editable editable) {
                meter1.setVisibility(View.VISIBLE);

            }

        });

        confirmpassword = findViewById(R.id.confirmpassword);
        mAuth = FirebaseAuth.getInstance();
        signupbtn = findViewById(R.id.button_signup);
        btn_logingoes = findViewById(R.id.button_login);
        reference = FirebaseDatabase.getInstance().getReference("credentials");
        signupbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PerformAuth();
            }
        });

        btn_logingoes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(registrationactivity.this,loginactivity.class);
                startActivity(intent);
            }
        });

        backbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(registrationactivity.this,loginactivity.class);
                startActivity(intent);
            }
        });



    }
    private void PerformAuth(){
        String username_string = Username.getText().toString();
        String email_string = signupemail.getText().toString();
        String password_string = signuppassword.getText().toString();
        String confirmpassword_string = confirmpassword.getText().toString();


        if (username_string.isEmpty()) {
            textInputLayoutForUsername.setError("Enter Username");
        } else if (!email_string.matches(emailPattern)) {
            textInputLayoutForEmail.setError("Enter Correct Email");
        } else if (password_string.isEmpty() || password_string.length() < 8) {
            textInputLayoutForpassword.setError("Enter Proper Password which is less than 8 ");
        } else if (confirmpassword_string.isEmpty()) {
            textInputLayoutforconfirmpassword.setError("Enter Password");

        }else if (!password_string.equals(confirmpassword_string)) {
            textInputLayoutforconfirmpassword.setError("Not match both password");
        }
        else {

            progressDialog.setMessage("Please Wait While Registration...");
            progressDialog.setTitle("Registration");
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.show();
            Integer marks =0;
            String image1 = null;
            mAuth.createUserWithEmailAndPassword(signupemail.getText().toString(), signuppassword.getText().toString())
                    .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                        @Override
                        public void onSuccess(AuthResult authResult) {

                            Users user = new Users(username_string, email_string, password_string, mAuth.getCurrentUser().getUid(),marks,image1);

                            reference.child(mAuth.getCurrentUser().getUid()).setValue(user)
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {

                                        }
                                    })
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void unused) {
                                            progressDialog.dismiss();
                                            signupemail.getText().clear();
                                            signuppassword.getText().clear();
                                            confirmpassword.getText().clear();
                                            Username.getText().clear();
                                            sendVerificationEmail();

                                        }
                                    });

                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            progressDialog.dismiss();
                            Toast.makeText(registrationactivity.this, e.toString(), Toast.LENGTH_SHORT).show();
                        }
                    });


        }

    }

    private void sendUserToNextActivity() {

        Intent intent = new Intent(registrationactivity.this, loginactivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);

    }
    private void sendVerificationEmail()
    {
        FirebaseAuth.getInstance().getCurrentUser()
        .sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            // email sent
                            Toast.makeText(registrationactivity.this,"Verify Your Email Before Login",Toast.LENGTH_SHORT).show();
                            // after email is sent just logout the user and finish this activity
                            FirebaseAuth.getInstance().signOut();
                            sendUserToNextActivity();

                        }
                        else
                        {
                            // email not sent, so display message and restart the activity or do whatever you wish to do
                            //restart this activity
                            overridePendingTransition(0, 0);
                            finish();
                            overridePendingTransition(0, 0);
                            startActivity(getIntent());

                        }
                    }
                });
    }






}