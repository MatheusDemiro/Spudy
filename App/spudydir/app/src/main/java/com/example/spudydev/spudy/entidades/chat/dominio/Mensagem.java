package com.example.spudydev.spudy.entidades.chat.dominio;

/**
 * Created by User on 05/02/2018.
 */

public class Mensagem {

    private String texto;
    private String autor;
    private String idMensagem;

    public String getTexto() {
        return texto;
    }

    public void setTexto(String texto) {
        this.texto = texto;
    }

    public String getAutor() {
        return autor;
    }

    public void setAutor(String autor) {
        this.autor = autor;
    }

    public String getIdMensagem() {
        return idMensagem;
    }

    public void setIdMensagem(String idMensagem) {
        this.idMensagem = idMensagem;
    }
}
