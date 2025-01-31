package com.example.handytalk;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.provider.MediaStore;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.yalantis.ucrop.UCrop;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class dashboard extends AppCompatActivity {
    private static final int REQUEST_CODE_PICK_IMAGE = 100;
    TextView name, email, usernameheader;
    ProgressDialog progressDialog;
    CircleImageView imageView;
    private final static int MY_PERMISSIONS_REQUEST_CAMERA = 12;
    private final static int MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE = 13;
    TextView govtscheme;
    FirebaseAuth firebaseAuth;
    Dialog dialog;
    private final List<String[]> dataList = new ArrayList<>();
    FirebaseUser firebaseUser;
    private CountDownTimer mCountDownTimer;
    NavigationView navigationView;
    private final int mProgressValue = 0;
    TextView newstxt;

    ActionBarDrawerToggle toggle;
    DrawerLayout drawerLayout;
    Toolbar toolbar;
    Uri personPhoto;
    CardView quizbtn, signtotext, contactusbtn, learnsignbtn;
    String userstring;
    Integer marks;
    private DatabaseReference reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        GoogleSignInAccount acct = GoogleSignIn.getLastSignedInAccount(getApplicationContext());
        if (acct != null) {
            String personName = acct.getDisplayName();
            String personEmail = acct.getEmail();
            personPhoto = acct.getPhotoUrl(); // this line to get  profile picture
        }
//        newstxt = (TextView) findViewById(R.id.news);
//        newstxt.setSelected(true);

        govtscheme = findViewById(R.id.govtscheme);
//        // Load the CSV file from the assets folder
//        loadCSV();
//
//        // Display the CSV data in a dialog box when a button is clicked
//        govtscheme.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                showCSVDialog();
//            }
//        });

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        quizbtn = findViewById(R.id.quizbtn);
        learnsignbtn = findViewById(R.id.learnsigncv);
        signtotext = findViewById(R.id.signtotext);
        name = findViewById(R.id.name);
        email = findViewById(R.id.email);
        contactusbtn = findViewById(R.id.contactus);
        progressDialog = new ProgressDialog(dashboard.this, R.style.AppCompatAlertDialogStyle);
        reference = FirebaseDatabase.getInstance().getReference("credentials");
        toolbar = findViewById(R.id.toolbardash);
        setSupportActionBar(toolbar);
        navigationView = findViewById(R.id.navmenu);
        navigationView.bringToFront();
        drawerLayout = findViewById(R.id.drawerlayout);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.Open, R.string.Close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        View headerView = navigationView.getHeaderView(0);
        imageView = headerView.findViewById(R.id.edit_photo);
        TextView mailtvheader = (TextView) headerView.findViewById(R.id.emailnav);
        usernameheader = (TextView) headerView.findViewById(R.id.Usernamenav);
        mailtvheader.setText(firebaseUser.getEmail());
        imageView.setImageURI(personPhoto);
        reference.child(firebaseUser.getUid())
                .addListenerForSingleValueEvent(listener);
        reference.child(firebaseUser.getUid())
                .addListenerForSingleValueEvent(listener1);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.home_menu:
                        Toast.makeText(dashboard.this, "Open Home", Toast.LENGTH_SHORT).show();
                        drawerLayout.closeDrawer(GravityCompat.START);
                        break;
                    case R.id.nav_upload_photo:
                        // Launch the gallery picker intent
                        Intent intent = new Intent(Intent.ACTION_PICK);
                        intent.setType("image/*");
                        startActivityForResult(intent, REQUEST_CODE_PICK_IMAGE);


                        return true;

                    case R.id.logout:
                        new AlertDialog.Builder(dashboard.this)
                                .setTitle("Logging Out")
                                .setMessage("Are you sure you want to logout?")
                                .setIcon(R.drawable.logot)

                                // Specifying a listener allows you to take an action before dismissing the dialog.
                                // The dialog is automatically dismissed when a dialog button is clicked.
                                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        FirebaseAuth.getInstance().signOut();
                                        Intent intent = new Intent(getApplicationContext(), loginactivity.class);
                                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                        startActivity(intent);
                                    }
                                })

                                // A null listener allows the button to dismiss the dialog and take no further action.
                                .setNegativeButton(android.R.string.no, null)
                                .setIcon(android.R.drawable.ic_dialog_alert)
                                .show();

                        break;


                }


                return true;
            }
        });


        quizbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), QuestionsActivity.class);
                startActivity(intent);
            }
        });


        signtotext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        contactusbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), contact_us.class);
                startActivity(intent);
            }
        });
        learnsignbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), learn_sign.class);
                startActivity(intent);
            }
        });


    }

    ValueEventListener listener = new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot snapshot) {

            if (snapshot.exists()) {
                String imageuri = snapshot.child("image").getValue(String.class);
                Glide.with(getApplicationContext()).load(imageuri).apply(RequestOptions.circleCropTransform()).into(imageView);

                userstring = snapshot.child("username").getValue(String.class);
                String firstName = "";
                if (userstring.split("\\w+").length > 1) {

                    firstName = userstring.substring(0, userstring.lastIndexOf(' '));
                } else {
                    firstName = userstring;
                }
                name.setText("Hello, " + firstName);
                usernameheader.setText(userstring);
            }


        }

        @Override
        public void onCancelled(@NonNull DatabaseError error) {

        }
    };


    ValueEventListener listener1 = new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot snapshot) {

            if (snapshot.exists()) {
                marks = snapshot.child("marks").getValue(Integer.class);
                govtscheme.setText("\uD83E\uDE99  "+marks);
                if (marks > 6) {
                    signtotext.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            // Initialize dialog
                            dialog = new Dialog(dashboard.this);
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
                                    if (ContextCompat.checkSelfPermission(dashboard.this,
                                            Manifest.permission.CAMERA)
                                            != PackageManager.PERMISSION_GRANTED) {
                                        if (!ActivityCompat.shouldShowRequestPermissionRationale(dashboard.this,
                                                Manifest.permission.CAMERA)) {
                                            ActivityCompat.requestPermissions(dashboard.this,
                                                    new String[]{Manifest.permission.CAMERA},
                                                    MY_PERMISSIONS_REQUEST_CAMERA);
                                        }
                                    }
                                    if (ContextCompat.checkSelfPermission(dashboard.this,
                                            Manifest.permission.WRITE_EXTERNAL_STORAGE)
                                            != PackageManager.PERMISSION_GRANTED) {
                                        if (!ActivityCompat.shouldShowRequestPermissionRationale(dashboard.this,
                                                Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                                            ActivityCompat.requestPermissions(dashboard.this,
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

                } else {
                    signtotext.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Toast.makeText(dashboard.this, "To Unlock this "+(7-marks)+" Extra Coins", Toast.LENGTH_SHORT).show();
                        }
                    });

                }
            }


        }

        @Override
        public void onCancelled(@NonNull DatabaseError error) {

        }
    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        if (requestCode == REQUEST_CODE_PICK_IMAGE && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri imageUri = data.getData();
            startCropActivity(imageUri);
            StorageReference storageRef = FirebaseStorage.getInstance().getReference().child("images/" + UUID.randomUUID().toString());
            UploadTask uploadTask = storageRef.putFile(imageUri);
            uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    // Image uploaded successfully, now store the download URL in Firebase Database
                    storageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            // Uri contains the download URL of the image, store it in Firebase Database
                            reference.child(firebaseUser.getUid()).child("image").setValue(uri.toString());
                            Glide.with(getApplicationContext()).load(uri.toString()).apply(RequestOptions.circleCropTransform()).into(imageView);

                        }
                    });
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    // Handle the failure case
                    Log.e(TAG, "Failed to upload image", e);
                }
            });
        }

        super.onActivityResult(requestCode, resultCode, data);
    }

    private void startCropActivity(Uri imageUri) {
        UCrop.Options options = new UCrop.Options();
        options.setToolbarTitle("Crop Image");

        UCrop.of(imageUri, Uri.fromFile(new File(getCacheDir(), "cropped_image")))
                .withAspectRatio(1, 1)
                .withOptions(options)
                .start(this);
    }

//    private void loadCSV() {
//        try {
//            // Open the CSV file from the assets folder
//            InputStream inputStream = getResources().openRawResource(R.raw.data);
//            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
//
//            // Read the CSV data into a list
//            String line;
//            while ((line = reader.readLine()) != null) {
//                String[] values = line.split(",");
//                dataList.add(values);
//            }
//
//            // Close the reader
//            reader.close();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }
//
//
//    private void showCSVDialog() {
//        // Create a dialog box to display the CSV data
//        AlertDialog.Builder builder = new AlertDialog.Builder(this);
//        builder.setTitle("Grants released Scheme of Sports & Games for the Disabled People");
//
//
//        // Create a horizontal scroll view to contain the table layout
//        HorizontalScrollView horizontalScrollView = new HorizontalScrollView(this);
//        ScrollView scrollView = new ScrollView(this);
//        scrollView.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 1400));
//
//        horizontalScrollView.addView(scrollView);
//
//        // Create a table to display the CSV data
//        TableLayout tableLayout = new TableLayout(this);
//        tableLayout.setHorizontalScrollBarEnabled(true);
//
//        // Add the CSV data to the table
//        for (int i = 0; i < dataList.size(); i++) {
//            TableRow tableRow = new TableRow(this);
//            String[] row = dataList.get(i);
//            for (int j = 0; j < row.length; j++) {
//                TextView textView = new TextView(this);
//                textView.setText(row[j]);
//                textView.setPadding(10, 10, 10, 10);
//                if (i == 0) { // set bold header for first row
//                    textView.setTypeface(null, Typeface.BOLD);
//                    textView.setTextColor(getResources().getColor(R.color.black));
//                }
//                if (j < row.length - 1) { // add vertical line to separate columns
//                    textView.setBackgroundResource(R.drawable.cell_border);
//                }
//                tableRow.addView(textView);
//            }
//            tableLayout.addView(tableRow);
//        }
//
//        // Add the table to the scroll view
//        scrollView.addView(tableLayout);
//
//        // Add the horizontal scroll view to the dialog box
//        builder.setView(horizontalScrollView);
//
//        // Display the dialog box
//
//        builder.show();
//    }



}

