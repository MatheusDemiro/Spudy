package com.example.spudydev.spudy.Entidades;

/**
 * Created by User on 08/01/2018.
 */

public class Material {
    private String nome;
    private String conteudo;

    public Material(String nome, String conteudo){
        this.nome = nome;
        this.conteudo = conteudo;
    }
    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }
    public String getConteudo() {
        return conteudo;
    }
    public void setConteudo(String conteudo) {
        this.conteudo = conteudo;
    }
}
