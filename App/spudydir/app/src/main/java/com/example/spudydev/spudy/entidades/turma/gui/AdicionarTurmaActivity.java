package com.example.spudydev.spudy.entidades.turma.gui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.spudydev.spudy.R;
import com.example.spudydev.spudy.entidades.aluno.gui.MainAlunoActivity;
import com.example.spudydev.spudy.entidades.turma.dominio.Turma;
import com.example.spudydev.spudy.infraestrutura.persistencia.AcessoFirebase;
import com.example.spudydev.spudy.infraestrutura.utils.Auxiliar;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

public class AdicionarTurmaActivity extends AppCompatActivity {

    private EditText edtCodigoTurma;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_aluno_adicionar_turma);

        edtCodigoTurma = findViewById(R.id.edtCodigoTurma);
        Button btnAdicionarTurma = findViewById(R.id.btnAdicionarTurma);
        btnAdicionarTurma.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                verificaTurma();
            }
        });
    }
    //verificando se o código da turma digitado é válido
    public void verificaTurma(){
        AcessoFirebase.getFirebase().addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                DataSnapshot refTurmaAluno = dataSnapshot.child("aluno").child(AcessoFirebase.getUidUsuario()).child("turmas").child(edtCodigoTurma.getText().toString());
               if (refTurmaAluno.exists()){
                   edtCodigoTurma.setError("Você já adicionou essa turma");
               }else {
                   DataSnapshot refTurma = dataSnapshot.child("turma").child(edtCodigoTurma.getText().toString());
                   if (refTurma.exists()){
                       adicionarTurmaAluno();
                   }else {
                       edtCodigoTurma.setError("Turma não existe");
                   }
               }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }
    //cadastrando o usuário na turma
    public void adicionarTurmaAluno(){
        Turma turma = new Turma();
        turma.adicionarTurma(edtCodigoTurma.getText().toString());
        abrirTelaMainAlunoActivity();
    }
    public void abrirTelaMainAlunoActivity(){
        Intent intent = new Intent(this, MainAlunoActivity.class);
        Auxiliar.criarToast(getApplicationContext(), "Turma adicionada com sucesso.");
        startActivity(intent);
        finish();
    }
}

