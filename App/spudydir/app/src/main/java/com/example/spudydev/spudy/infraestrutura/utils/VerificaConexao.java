package com.example.spudydev.spudy.infraestrutura.utils;

import android.app.Service;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * The type Verifica conexao.
 */
public class VerificaConexao {

    private Context context;

    /**
     * Instantiates a new Verifica conexao.
     *
     * @param context1 the context 1
     */
    public VerificaConexao(Context context1) {
        this.context = context1;
    }

    /**
     * Esta conectado boolean.
     *
     * @return the boolean
     */
    public boolean estaConectado(){
        ConnectivityManager connectivity = (ConnectivityManager)
                context.getSystemService(Service.CONNECTIVITY_SERVICE);
        if (connectivity != null){
            NetworkInfo info = connectivity.getActiveNetworkInfo();
            if (info != null){
                return info.getState() == NetworkInfo.State.CONNECTED;
            }
        }
        return false;
    }

}
