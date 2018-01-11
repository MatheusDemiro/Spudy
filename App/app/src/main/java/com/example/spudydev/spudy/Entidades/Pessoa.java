package com.example.spudydev.spudy.Entidades;

/**
 * Created by User on 10/01/2018.
 */
import com.google.firebase.database.Exclude;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;

public class Pessoa {
    private String nome;
    private String data_nascimento;
    private String idUsuario;
    private Usuario usuario;

    public Pessoa(){
    }
    //Criando HashMap para jogar no banco
    @Exclude
    public Map<String, Object> toMapPessoa() {
        HashMap<String, Object> hashMapUsuario = new HashMap<>();
        hashMapUsuario.put("nome", this.getNome());
        hashMapUsuario.put("dataNascimento", this.getData_nascimento());
        hashMapUsuario.put("idUsuario", this.getUser().getId());

        return hashMapUsuario;
    }

    public String getData_nascimento() { return data_nascimento; }
    public void setData_nascimento(String data_nascimento) { this.data_nascimento = data_nascimento; }
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
