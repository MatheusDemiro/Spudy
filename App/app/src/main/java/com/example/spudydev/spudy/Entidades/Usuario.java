package com.example.spudydev.spudy.Entidades;

import com.google.firebase.database.Exclude;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Matheus on 10/12/2017.
 */
    //
public class Usuario {
    private String email;
    private String senha;
    private String id;
    private String tipoConta;
    private String instituicao;

    public Usuario() {
    }
    //Criando o HasMap para enviar ao banco
    @Exclude
    public Map<String, Object> toMapUsuario() {
        HashMap<String, Object> hashMapUsuario = new HashMap<>();
        hashMapUsuario.put("id", getId());
        hashMapUsuario.put("email", getEmail());
        hashMapUsuario.put("tipoConta", getTipoConta());
        hashMapUsuario.put("instituicao", getInstituicao());

        return hashMapUsuario;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {

        this.email = email;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {

        this.id = id;
    }

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


