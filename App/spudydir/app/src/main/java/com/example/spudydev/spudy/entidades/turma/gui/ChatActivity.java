package com.example.spudydev.spudy.entidades.turma.gui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.spudydev.spudy.R;
import com.example.spudydev.spudy.entidades.material.gui.MainMaterialAlunoActivity;
import com.example.spudydev.spudy.entidades.material.gui.MainMaterialProfessorActivity;
import com.example.spudydev.spudy.entidades.turma.gui.UsuariosTurmaAlunoActivity;
import com.example.spudydev.spudy.entidades.turma.gui.UsuariosTurmaProfessorActivity;
import com.example.spudydev.spudy.infraestrutura.persistencia.AcessoFirebase;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

public class ChatActivity extends AppCompatActivity {

    private TextView tvNomeTurma;
    private String codigoTurma;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        getSupportActionBar().hide();


        tvNomeTurma = findViewById(R.id.tvChatNomeTurma);
        Button btnChatUsuario = findViewById(R.id.btnChatUsuario);
        Button btnChatMateriais = findViewById(R.id.btnChatMateriais);
        codigoTurma = getIntent().getStringExtra("codigoTurma");

        carregaNomeTurma();

        btnChatUsuario.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                abrirTelaUsuarios();
            }
        });

        btnChatMateriais.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                abrirTelaMaterial();
            }
        });
    }
    public void carregaNomeTurma(){
        AcessoFirebase.getFirebase().child("turma").child(codigoTurma).child("nomeTurma").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                tvNomeTurma.setText(dataSnapshot.getValue(String.class));
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
    //Intent
    public void abrirTelaMaterial(){
        AcessoFirebase.getFirebase().child("usuario").child(AcessoFirebase.getUidUsuario()).child("tipoConta").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.getValue().toString().equals("Aluno")) {
                    Intent intent = new Intent(getApplicationContext(), MainMaterialAlunoActivity.class);
                    intent.putExtra("nomeTurma", tvNomeTurma.getText().toString());
                    intent.putExtra("codigoTurma",codigoTurma);
                    startActivity(intent);
                } else {
                    Intent intent = new Intent(getApplicationContext(), MainMaterialProfessorActivity.class);
                    intent.putExtra("nomeTurma", tvNomeTurma.getText().toString());
                    intent.putExtra("codigoTurma",codigoTurma);
                    startActivity(intent);
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });

    }
    public void abrirTelaUsuarios(){
        AcessoFirebase.getFirebase().child("usuario").child(AcessoFirebase.getUidUsuario()).child("tipoConta").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                //verificando o tipo de conta para direcionar o usuário a sua respectiva tela
                if (dataSnapshot.getValue().toString().equals("Aluno")){
                    Intent intent = new Intent(getApplicationContext(), UsuariosTurmaAlunoActivity.class);
                    intent.putExtra("codigoTurma",codigoTurma);
                    startActivity(intent);
                }else{
                    Intent intent = new Intent(getApplicationContext(), UsuariosTurmaProfessorActivity.class);
                    intent.putExtra("codigoTurma",codigoTurma);
                    startActivity(intent);
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }
}