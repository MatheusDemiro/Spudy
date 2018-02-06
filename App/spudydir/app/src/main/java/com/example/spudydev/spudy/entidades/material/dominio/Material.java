package com.example.spudydev.spudy.entidades.material.dominio;

import java.util.HashMap;
import java.util.Map;

public class Material {

    private String nome;
    private String conteudo;

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

    public Map<String, Object> toMapMaterial() {
        HashMap<String, Object> hashMapMaterial = new HashMap<>();
        hashMapMaterial.put("nome", getNome());
        hashMapMaterial.put("conteudo", getConteudo());
        return hashMapMaterial;
    }

}
