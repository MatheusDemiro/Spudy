package com.example.spudydev.spudy.entidades.falta.dominio;

/**
 * Created by User on 05/02/2018.
 */

public class Falta {
    private boolean presenca;
    private String data;

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public boolean isPresenca() {
        return presenca;
    }

    public void setPresenca(boolean presenca) {
        this.presenca = presenca;
    }
}
