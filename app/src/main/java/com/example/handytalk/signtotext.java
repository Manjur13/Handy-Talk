package com.example.handytalk;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.util.Locale;
import java.util.Set;
import java.util.UUID;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Observer;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class signtotext extends AppCompatActivity {
    // Global variables we will use in the
    @NonNull Observable<Object> connectToBTObservable;
    private static final String TAG = "FrugalLogs";
    private static final int REQUEST_ENABLE_BT = 1;
    TextToSpeech textToSpeech;
    ImageView tvquelogo;
    //We will use a Handler to get the BT Connection statys
    public static Handler handler;
    private final static int ERROR_READ = 0; // used in bluetooth handler to identify message update
    BluetoothDevice arduinoBTModule = null;
    UUID arduinoUUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB"); //We declare a default UUID to create the global variable

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signtotext);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Sign to Text Usign Gloves");
        tvquelogo = findViewById(R.id.tvque);

        //Intances of BT Manager and BT Adapter needed to work with BT in Android.
        BluetoothManager bluetoothManager = getSystemService(BluetoothManager.class);
        BluetoothAdapter bluetoothAdapter = bluetoothManager.getAdapter();
        //Intances of the Android UI elements that will will use during the execution of the APP
        TextView btReadings = findViewById(R.id.btReadings);
        TextView glovesstatus = findViewById(R.id.sensetv);
        Button connectToDevice = (Button) findViewById(R.id.connectToDevice);
        ImageView seachDevices = (ImageView) findViewById(R.id.seachDevices);
        Button clearValues = (Button) findViewById(R.id.refresh);
        ImageView ttlbtn = (ImageView) findViewById(R.id.texttospeech);
        ImageView copytxtbtn = (ImageView) findViewById(R.id.copytext);

        Log.d(TAG, "Begin Execution");
        //Using a handler to update the interface in case of an error connecting to the BT device
        //My idea is to show handler vs RxAndroid
        handler = new Handler(Looper.getMainLooper()) {
            @Override
            public void handleMessage(Message msg) {
                switch (msg.what) {

                    case ERROR_READ:
                        String arduinoMsg = msg.obj.toString(); // Read message from Arduino
                        btReadings.setText(arduinoMsg);
                        break;
                }
            }
        };

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


        // Create an Observable from RxAndroid
        //The code will be executed when an Observer subscribes to the the Observable
        connectToBTObservable = io.reactivex.rxjava3.core.Observable.create(emitter -> {
            Log.d(TAG, "Calling connectThread class");
            //Call the constructor of the ConnectThread class
            //Passing the Arguments: an Object that represents the BT device,
            // the UUID and then the handler to update the UI
            ConnectThread connectThread = new ConnectThread(arduinoBTModule, arduinoUUID, handler);
            connectThread.run();
            //Check if Socket connected
            if (connectThread.getMmSocket().isConnected()) {
                Log.d(TAG, "Calling ConnectedThread class");
                //The pass the Open socket as arguments to call the constructor of ConnectedThread
                ConnectedThread connectedThread = new ConnectedThread(connectThread.getMmSocket());
                connectedThread.run();
                if (connectedThread.getValueRead() != null) {
                    // If we have read a value from the Arduino
                    // we call the onNext() function
                    //This value will be observed by the observer
                    emitter.onNext(connectedThread.getValueRead());
                }

                //We just want to stream 1 value, so we close the BT stream
//                connectedThread.cancel();
            }
            // SystemClock.sleep(5000); // simulate delay
            //Then we close the socket connection
            connectThread.cancel();
            //We could Override the onComplete function
            emitter.onComplete();

        });

        connectToDevice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btReadings.setText("");

                if (arduinoBTModule != null) {
                    //We subscribe to the observable until the onComplete() is called
                    //We also define control the thread management with
                    // subscribeOn:  the thread in which you want to execute the action
                    // observeOn: the thread in which you want to get the response
                    connectToBTObservable
                            .subscribeOn(Schedulers.io()) // execute on IO thread
                            .observeOn(AndroidSchedulers.mainThread()) // observe on main thread
                            .subscribe(new Observer<Object>() {
                                @Override
                                public void onSubscribe(Disposable d) {
                                    // called when the observer is subscribed to the observable
                                }

                                @Override
                                public void onNext(Object value) {
                                    // called when the observable emits a value
                                    String stringValue = value.toString().trim();
                                    btReadings.setText(stringValue);
                                    switch (stringValue) {
                                        case "Are you free?":
                                            tvquelogo.setImageResource(R.drawable.one);
                                            break;
                                        case "Welcome to Tech Expo!":
                                            tvquelogo.setImageResource(R.drawable.two);
                                            break;
                                        case "I'm Okay...":
                                            tvquelogo.setImageResource(R.drawable.okay);
                                            break;
                                    }
                                }

                                @Override
                                public void onError(Throwable e) {
                                    // called when the observable encounters an error
                                }

                                @Override
                                public void onComplete() {

                                }
                            });
//                    connectToBTObservable.observeOn(AndroidSchedulers.mainThread()).
//                            subscribeOn(Schedulers.io()).
//                            subscribe(valueRead -> {
//                                //valueRead returned by the onNext() from the Observable
////                                btReadings.setText((CharSequence) valueRead);
//                                String arduinoData = (String) valueRead;
//                                btReadings.setText(arduinoData);
//                                // Compare the arduinoData with your string
//                                String myString = "Are you free?";
//                                if (arduinoData.equals(myString))
//                                {
//                                    Toast.makeText(signtotext.this, "WORKING", Toast.LENGTH_SHORT).show();
//                                }
//                            });
                }

            }
        });
        ttlbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                textToSpeech.speak(btReadings.getText().toString(), TextToSpeech.QUEUE_FLUSH, null);
            }
        });



        copytxtbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ClipboardManager clipboardManager = (ClipboardManager)
                        getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clipData = ClipData.newPlainText("nonsense_data",
                        btReadings.getText().toString());
                clipboardManager.setPrimaryClip(clipData);
                Toast.makeText(getApplicationContext(), "COPIED YOUR TEXT", Toast.LENGTH_SHORT).show();
            }
        });

        // Set a listener event on a button to clear the texts
        clearValues.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                btDevices.setText("");
                btReadings.setText("");
                if (arduinoBTModule != null) {
                    //We subscribe to the observable until the onComplete() is called
                    //We also define control the thread management with
                    // subscribeOn:  the thread in which you want to execute the action
                    // observeOn: the thread in which you want to get the response
                    connectToBTObservable
                            .subscribeOn(Schedulers.io()) // execute on IO thread
                            .observeOn(AndroidSchedulers.mainThread()) // observe on main thread
                            .subscribe(new Observer<Object>() {
                                @Override
                                public void onSubscribe(Disposable d) {
                                    // called when the observer is subscribed to the observable
                                }

                                @Override
                                public void onNext(Object value) {
                                    // called when the observable emits a value
                                    Log.d("MyApp", "onNext() called with value: " + value);
                                    String stringValue = value.toString().trim();
                                    btReadings.setText(stringValue);
                                    Log.d("MyApp", "Value emitted: " + stringValue);
                                    switch (stringValue) {
                                        case "What is your name?":
                                            tvquelogo.setImageResource(R.drawable.ten);
                                            break;
                                        case "Welcome to Handy Talk Community!":
                                            tvquelogo.setImageResource(R.drawable.eleven);
                                            break;
                                        case "I'm Okay...":
                                            tvquelogo.setImageResource(R.drawable.okay);
                                            break;

                                    }
                                }

                                @Override
                                public void onError(Throwable e) {
                                    // called when the observable encounters an error
                                }

                                @Override
                                public void onComplete() {

                                }
                            });

                }

            }
        });

        seachDevices.setOnClickListener(new View.OnClickListener() {
            //Display all the linked BT Devices
            @Override
            public void onClick(View view) {
                //Check if the phone supports BT
                if (bluetoothAdapter == null) {
                    // Device doesn't support Bluetooth
                    Log.d(TAG, "Device doesn't support Bluetooth");
                } else {
                    Log.d(TAG, "Device support Bluetooth");
                    //Check BT enabled. If disabled, we ask the user to enable BT
                    if (!bluetoothAdapter.isEnabled()) {
                        Log.d(TAG, "Bluetooth is disabled");
                        Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                        if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
                            // TODO: Consider calling
                            //    ActivityCompat#requestPermissions
                            // here to request the missing permissions, and then overriding
                            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                            //                                          int[] grantResults)
                            // to handle the case where the user grants the permission. See the documentation
                            // for ActivityCompat#requestPermissions for more details.
                            Log.d(TAG, "We don't BT Permissions");
                            startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
                            Log.d(TAG, "Bluetooth is enabled now");
                        } else {
                            Log.d(TAG, "We have BT Permissions");
                            startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
                            Log.d(TAG, "Bluetooth is enabled now");
                        }

                    } else {
                        Log.d(TAG, "Bluetooth is enabled");
                    }
                    String btDevicesString = "";
                    Set<BluetoothDevice> pairedDevices = bluetoothAdapter.getBondedDevices();

                    if (pairedDevices.size() > 0) {
                        // There are paired devices. Get the name and address of each paired device.
                        for (BluetoothDevice device : pairedDevices) {
                            String deviceName = device.getName();
                            String deviceHardwareAddress = device.getAddress(); // MAC address
                            Log.d(TAG, "deviceName:" + deviceName);
                            Log.d(TAG, "deviceHardwareAddress:" + deviceHardwareAddress);
                            //We append all devices to a String that we will display in the UI
                            btDevicesString = btDevicesString + deviceName + " || " + deviceHardwareAddress + "\n";
                            //If we find the HC 05 device (the Arduino BT module)
                            //We assign the device value to the Global variable BluetoothDevice
                            //We enable the button "Connect to HC 05 device"
                            if (deviceName.equals("HC-05")) {
                                Log.d(TAG, "HC-05 found");
                                glovesstatus.setText("Smart Gloves Connected \uD83D\uDFE2");
                                arduinoUUID = device.getUuids()[0].getUuid();
                                arduinoBTModule = device;
                                //HC -05 Found, enabling the button to read results
                                connectToDevice.setEnabled(true);
                            }
//                            btDevices.setText(btDevicesString);
                        }
                    }
                }
                Log.d(TAG, "Button Pressed");
            }
        });

    }

    @Override
    public boolean onSupportNavigateUp() {
        Intent in = new Intent(getApplicationContext(), dashboard.class);
        startActivity(in);
        return true;
    }
}