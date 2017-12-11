package com.example.spudydev.spudy.Entidades;

import com.example.spudydev.spudy.DAO.ConfiguracaoFirebase;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Exclude;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Matheus on 10/12/2017.
 */

public class Usuarios {
    private String id;
    private String nome;
    private String dataNasc;
    private String email;
    private String senha;
    private String perguntaSeguranca;
    private String tipoConta;
    private String resposta;
    private String instituicao;

    public Usuarios() {
    }
    //BD ENVIAR DADOS
    public void salvar(){
        DatabaseReference referenciaFirebase = ConfiguracaoFirebase.getFirebase();
        referenciaFirebase.child("usuario").child(String.valueOf(getId())).setValue(this);

    }
    @Exclude

    public Map<String,Object> toMap(){
        HashMap<String, Object> hashMapUsuario = new HashMap<>();

        hashMapUsuario.put("id",getId());
        hashMapUsuario.put("nome",getNome());
        hashMapUsuario.put("dataNasc",getDataNasc());
        hashMapUsuario.put("email",getEmail());
        hashMapUsuario.put("senha",getSenha());
        hashMapUsuario.put("perguntaSeguranca",getPerguntaSeguranca());
        hashMapUsuario.put("tipoConta", getTipoConta());
        hashMapUsuario.put("resposta",getResposta());
        hashMapUsuario.put("instituicao",getInstituicao());

        return hashMapUsuario;
    }
    //FIM BD ENVIAR DADOS

    public String getInstituicao() {
        return instituicao;
    }

    public void setInstituicao(String instituicao) {
        this.instituicao = instituicao;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getDataNasc() {
        return dataNasc;
    }

    public void setDataNasc(String dataNasc) {
        this.dataNasc = dataNasc;
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

    public String getPerguntaSeguranca() {
        return perguntaSeguranca;
    }

    public void setPerguntaSeguranca(String perguntaSeguranca) { this.perguntaSeguranca = perguntaSeguranca; }

    public String getTipoConta() { return tipoConta; }

    public void setTipoConta(String conta) {
        this.tipoConta = conta;
    }

    public String getResposta() {
        return resposta;
    }

    public void setResposta(String resposta) {
        this.resposta = resposta;
    }
}

