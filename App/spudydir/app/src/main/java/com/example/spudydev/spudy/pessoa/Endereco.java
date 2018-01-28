package com.example.spudydev.spudy.pessoa;

public class Endereco {

    private String endereco;
    private String complemento;
    private String cidade;
    private String estado;

    public Endereco() {
        this.endereco = endereco;
        this.complemento = complemento;
        this.cidade = cidade;
        this.estado = estado;
    }

    public String getEndereco() {
        return endereco;
    }

    public void setEndereco(String endereco) {
        this.endereco = endereco;
    }

    public String getComplemento() {
        return complemento;
    }

    public void setComplemento(String complemento) {
        this.complemento = complemento;
    }

    public String getCidade() {
        return cidade;
    }

    public void setCidade(String cidade) {
        this.cidade = cidade;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }
}
