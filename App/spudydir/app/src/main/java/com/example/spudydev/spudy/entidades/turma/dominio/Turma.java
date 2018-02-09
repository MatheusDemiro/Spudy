package com.example.spudydev.spudy.entidades.turma.dominio;

import com.example.spudydev.spudy.entidades.professor.dominio.Professor;

import java.util.HashMap;


public class Turma {

    private String nome;
    private Professor professor;

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public Professor getProfessor() {
        return professor;
    }

    public void setProfessor(Professor professor) {
        this.professor = professor;
    }

    //Esta chamada vai ser feita pelo PROFESSOR
    public HashMap<String, Object> toMapTurma(){
        HashMap<String, Object> hashMapTurma = new HashMap<>();
        hashMapTurma.put("nomeTurma", getNome());

        return hashMapTurma;
    }
}