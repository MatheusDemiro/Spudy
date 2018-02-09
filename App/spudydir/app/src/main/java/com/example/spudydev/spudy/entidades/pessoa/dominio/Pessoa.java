package com.example.spudydev.spudy.entidades.pessoa.dominio;

public class Pessoa {

    private String nome;
    private String dataNascimento;

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
}
