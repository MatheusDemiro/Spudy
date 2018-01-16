package com.example.spudydev.spudy.entidades;

import java.util.ArrayList;

/**
 * Created by User on 08/01/2018.
 */

public class Disciplina {
    private String nome;
    private ArrayList<Material> materiais;

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }
    public void adicionarMaterial(Material material){
        materiais.add(material);
    }
}
