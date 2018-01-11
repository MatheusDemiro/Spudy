package com.example.spudydev.spudy.Entidades;

/**
 * Created by User on 10/01/2018.
 */
import com.google.firebase.database.Exclude;

import java.util.HashMap;
import java.util.Map;

public class Pessoa {
    private String nome;
    private String dataNascimento;
    private String idUsuario;
    private Usuario usuario;

    public Pessoa(){
    }
    //Criando HashMap para jogar no banco
    @Exclude
    public Map<String, Object> toMapPessoa() {
        HashMap<String, Object> hashMapUsuario = new HashMap<>();
        hashMapUsuario.put("nome", this.getNome());
        hashMapUsuario.put("dataNascimento", this.getDataNascimento());
        hashMapUsuario.put("idUsuario", this.getUser().getId());

        return hashMapUsuario;
    }

    public String getDataNascimento() { return dataNascimento; }
    public void setDataNascimento(String dataNascimento) { this.dataNascimento = dataNascimento; }
    public String getNome() {
        return nome;
    }
    public void setNome(String nome) {
        this.nome = nome;
    }
    public void setUsuario(Usuario usuario){
        this.usuario = usuario;
    }
    public Usuario getUser(){
        return usuario;
    }
    public String getIdUsuario() { return idUsuario;  }
    public void setIdUsuario(String idUsuario) { this.idUsuario = idUsuario; }
}
