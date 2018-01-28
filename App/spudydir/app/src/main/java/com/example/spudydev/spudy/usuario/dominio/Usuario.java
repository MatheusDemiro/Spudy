package com.example.spudydev.spudy.usuario.dominio;

import com.google.firebase.database.Exclude;

import java.util.HashMap;
import java.util.Map;

public class Usuario {

    private String email;
    private String tipoConta;
    private String instituicao;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) { this.email = email; }

    public String getTipoConta() {
        return tipoConta;
    }

    public void setTipoConta(String tipoConta) {
        this.tipoConta = tipoConta;
    }

    public String getInstituicao() {
        return instituicao;
    }

    public void setInstituicao(String instituicao) {
        this.instituicao = instituicao;
    }

}

