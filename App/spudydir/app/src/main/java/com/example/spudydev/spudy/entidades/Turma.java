package com.example.spudydev.spudy.entidades;

import com.example.spudydev.spudy.usuario.dominio.Usuario;

import java.util.ArrayList;

/**
 * Created by User on 08/01/2018.
 */

public class Turma {
    private Usuario professor;
    private ArrayList<Usuario> alunos;
    private String id;

    public Usuario getProfessor() {
        return professor;
    }

    public void setProfessor(Usuario professor) {
        this.professor = professor;
    }
}
