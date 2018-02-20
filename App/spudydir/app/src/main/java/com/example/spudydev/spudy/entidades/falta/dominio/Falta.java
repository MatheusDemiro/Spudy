package com.example.spudydev.spudy.entidades.falta.dominio;

/**
 * Created by User on 05/02/2018.
 */
public class Falta {
    private boolean presenca;
    private String data;

    /**
     * Gets data.
     *
     * @return the data
     */
    public String getData() {
        return data;
    }

    /**
     * Sets data.
     *
     * @param string the string
     */
    public void setData(String string) {
        this.data = string;
    }

    /**
     * Is presenca boolean.
     *
     * @return the boolean
     */
    public boolean isPresenca() {
        return presenca;
    }

    /**
     * Sets presenca.
     *
     * @param booleano the booleano
     */
    public void setPresenca(boolean booleano) {
        this.presenca = booleano;
    }
}
