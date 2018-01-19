package com.example.spudydev.spudy.usuario.dominio;

import com.google.firebase.database.Exclude;

import java.util.HashMap;
import java.util.Map;

    //
public class Usuario {
    private String email;
    private String senha;
    private String tipoConta;
    private String instituicao;

    //Criando o HasMap para enviar ao banco
    @Exclude
    public Map<String, Object> toMapUsuario() {
        HashMap<String, Object> hashMapUsuario = new HashMap<>();
        hashMapUsuario.put("email", getEmail());
        hashMapUsuario.put("tipoConta", getTipoConta());
        hashMapUsuario.put("instituicao", getInstituicao());

        return hashMapUsuario;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) { this.email = email; }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public String getTipoConta() { return tipoConta; }

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


