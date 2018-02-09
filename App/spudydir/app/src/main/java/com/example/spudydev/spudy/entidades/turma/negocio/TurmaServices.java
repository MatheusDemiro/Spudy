package com.example.spudydev.spudy.entidades.turma.negocio;

import android.content.Context;
import android.content.Intent;
import com.example.spudydev.spudy.entidades.aluno.gui.MainAlunoActivity;
import com.example.spudydev.spudy.entidades.professor.gui.MainProfessorActivity;
import com.example.spudydev.spudy.entidades.turma.dominio.Turma;
import com.example.spudydev.spudy.entidades.turma.persistência.TurmaDAO;
import com.example.spudydev.spudy.infraestrutura.persistencia.AcessoFirebase;
import com.example.spudydev.spudy.infraestrutura.utils.Auxiliar;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

public class TurmaServices {
    public static void criarTurmaProfessor(Context context, Turma turma){
        if (TurmaDAO.criarTurma(turma)){
            context.startActivity(new Intent(context, MainProfessorActivity.class));
        }
    }
    //verificando se o código da turma digitado é válido
    public static void criarTurmaAluno(final String idTurma, final Context context){
        AcessoFirebase.getFirebase().addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                DataSnapshot refTurmaAluno = dataSnapshot.child("aluno").child(AcessoFirebase.getUidUsuario()).child("turmas").child(idTurma);
                if (refTurmaAluno.exists()){
                    Auxiliar.criarToast(context, "Você já adicionou essa turma");
                }else {
                    DataSnapshot refTurma = dataSnapshot.child("turma").child(idTurma);
                    if (refTurma.exists()){
                        adicionarTurmaAluno(idTurma, context);
                    }else {
                        Auxiliar.criarToast(context,"Turma não existe");
                    }
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }
    //Cadastrando o usuário na turma
    private static void adicionarTurmaAluno(String idTurma, Context context){
        if (TurmaDAO.adicionarTurma(idTurma)){
            Auxiliar.criarToast(context, "Turma adicionada com sucesso!");
            context.startActivity(new Intent(context, MainAlunoActivity.class));
        }else {
            Auxiliar.criarToast(context, "Erro ao adicionar turma.Verifique conexão com a internet ou contate o suporte técnico.");
        }
    }
}
