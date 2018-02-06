package com.example.spudydev.spudy.entidades.turma.dominio;

import android.content.Context;

import com.example.spudydev.spudy.infraestrutura.persistencia.AcessoFirebase;
import com.example.spudydev.spudy.infraestrutura.utils.Auxiliar;
import com.example.spudydev.spudy.infraestrutura.utils.MD5;
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

    private HashMap<String, Object> toMapTurma(){
        HashMap<String, Object> hashMapTurma = new HashMap<>();
        hashMapTurma.put("nomeTurma", getNome());

        return hashMapTurma;
    }
    //Professor Cria turma
    public String criarTurma(String nomedaturma){
        //Adicionar verificação turma com mesmo nome
        this.setNome(nomedaturma);

        String uid = AcessoFirebase.getUidUsuario();
        String codigoTurma = AcessoFirebase.getFirebase().child("turma").push().getKey();

        //Salvando a turma na árvore turma
        AcessoFirebase.getFirebase().child("turma").child(codigoTurma).setValue(this.toMapTurma());
        //Salvando a turma na árvore professor
        AcessoFirebase.getFirebase().child("professor").child(uid).child("turmasMinistradas").child(codigoTurma).setValue(codigoTurma);
        //Add professor usuariosDaTurma
        AcessoFirebase.getFirebase().child("turma").child(codigoTurma).child("usuariosDaTurma").child(uid).setValue(uid);

        return codigoTurma;
    }

    //Professor Criar turma
    public void adicionarTurma(String codigoTurma){
        //Adicionar verificação se aluno já esta na turma
        String uid = AcessoFirebase.getUidUsuario();
        AcessoFirebase.getFirebase().child("aluno").child(uid).child("turmas").child(codigoTurma).setValue(codigoTurma);
        AcessoFirebase.getFirebase().child("turma").child(codigoTurma).child("usuariosDaTurma").child(uid).setValue(uid);
    }
}