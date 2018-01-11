package com.example.spudydev.spudy.Verificador;

import android.app.Service;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.example.spudydev.spudy.DAO.PerfilDAO;

public class VerificaConexao {

    Context context;

    public VerificaConexao(Context context) {

        this.context = context;
    }

    public boolean estaConectado(){
        ConnectivityManager connectivity = (ConnectivityManager)
                context.getSystemService(Service.CONNECTIVITY_SERVICE);
        if (connectivity != null){
            NetworkInfo info = connectivity.getActiveNetworkInfo();
            if (info != null){
                if (info.getState() == NetworkInfo.State.CONNECTED){
                    return true;
                }
            }
        }
        return false;
    }

}
