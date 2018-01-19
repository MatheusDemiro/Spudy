package com.example.spudydev.spudy.pessoa.dominio;

import com.example.spudydev.spudy.usuario.dominio.Usuario;
import com.google.firebase.database.Exclude;

import java.util.HashMap;
import java.util.Map;

public class Pessoa {
    private String nome;
    private String dataNascimento;
    private Usuario usuario;

    //Criando HashMap para jogar no banco
    @Exclude
    public Map<String, Object> toMapPessoa() {
        HashMap<String, Object> hashMapUsuario = new HashMap<>();
        hashMapUsuario.put("nome", this.getNome());
        hashMapUsuario.put("dataNascimento", this.getDataNascimento());
        return hashMapUsuario;
    }

    public String getDataNascimento() { return dataNascimento; }
    public void setDataNascimento(String dataNascimento) {
        this.dataNascimento = dataNascimento;
    }
    public String getNome() {
        return nome;
    }
    public void setNome(String nome) {
        this.nome = nome;
    }
    public void setUsuario(Usuario usuario){
        this.usuario = usuario;
    }
    public Usuario getUsuario(){
        return usuario;
    }
}
