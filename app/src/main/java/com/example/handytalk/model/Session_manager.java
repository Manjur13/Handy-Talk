package com.example.handytalk.model;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

public class Session_manager {

    private static final String TAG = Session_manager.class.getSimpleName();
    SharedPreferences pref;
    SharedPreferences.Editor editor;
    Context _context;

    int PRIVATE_MODE = 0;

    private static final String PREF_NAME = "Manjurloginin";
    private static final String KEY_IS_LOGGED_IN = "isLoggedIn";

    public Session_manager(Context context){

        this._context = context;
        pref = _context.getSharedPreferences(PREF_NAME,PRIVATE_MODE);
        editor = pref.edit();

    }

    public void setLogin(boolean isLoggedIn)
    {
        editor.putBoolean(KEY_IS_LOGGED_IN,isLoggedIn);
        editor.commit();
        Log.d(TAG,"User login session modified");

    }

    public boolean isLoggedIn()
    {
        return pref.getBoolean(KEY_IS_LOGGED_IN,false);
    }





}

