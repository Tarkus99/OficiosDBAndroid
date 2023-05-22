package com.example.myrecyclerviewexample;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.preference.PreferenceManager;

public class GestionarPreferencias {
    private SharedPreferences sharedPreferences;
    private static GestionarPreferencias gestionarPreferencias;

    private GestionarPreferencias(){}

    public static GestionarPreferencias getInstance(){
        if (gestionarPreferencias==null){
            return new GestionarPreferencias();
        }
        return gestionarPreferencias;
    }

    public void inicializa(Context context){
        if (sharedPreferences==null){
            sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        }
    }

    public String getIpConnection(Context context){
        inicializa(context);
        return sharedPreferences.getString("ipConnection", "10.13.0.4");
    }
}
