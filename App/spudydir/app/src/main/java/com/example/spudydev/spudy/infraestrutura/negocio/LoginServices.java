package com.example.spudydev.spudy.infraestrutura.negocio;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.widget.Toast;

import com.example.spudydev.spudy.R;
import com.example.spudydev.spudy.infraestrutura.persistencia.AcessoFirebase;
import com.example.spudydev.spudy.usuario.aluno.gui.MainAlunoActivity;
import com.example.spudydev.spudy.usuario.professor.gui.MainProfessorActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;


public class LoginServices {

    public static void emailSenha(String CampoEmail, String CampoSenha, final Context context){

        AcessoFirebase.getFirebaseAutenticacao().signInWithEmailAndPassword(CampoEmail, CampoSenha).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){
                    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                    if (user != null){
                        AcessoFirebase.getFirebase().child("usuario").child(user.getUid()).child("tipoConta").addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                String tipoConta = dataSnapshot.getValue(String.class);
                                if (dataSnapshot.getValue(String.class) != null){
                                    if(tipoConta.equals("Aluno")) {
                                        abrirTelaAluno(context);
                                        Toast.makeText(context, R.string.sp_login_bem_sucedido, Toast.LENGTH_SHORT).show();
                                    } else {
                                        abrirTelaProfessor(context);
                                        Toast.makeText(context, R.string.sp_login_bem_sucedido, Toast.LENGTH_SHORT).show();
                                    }
                                }
                            }
                            @Override
                            public void onCancelled(DatabaseError databaseError) {
                            }
                        });
                    }else {
                        Toast.makeText(context, "Usuário não encontrado.", Toast.LENGTH_SHORT).show();
                    }
                }else{
                    Toast.makeText(context, "Usuário ou senha inválido", Toast.LENGTH_SHORT).show();
                }

            }
        });
    }

    public static void abrirTelaAluno(Context context){
        //Iniciando tela do Aluno
        Intent intentAbrirTelaAluno = new Intent(context, MainAlunoActivity.class);
        intentAbrirTelaAluno.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intentAbrirTelaAluno);
    }
    public static void abrirTelaProfessor(Context context) {
        //Iniciando tela do Professor
        Intent intentAbrirTelaProfessor = new Intent(context, MainProfessorActivity.class);
        intentAbrirTelaProfessor.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intentAbrirTelaProfessor);
    }
}
