package com.example.spudydev.spudy.entidades.turma.gui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import com.example.spudydev.spudy.R;
import com.example.spudydev.spudy.entidades.turma.dominio.Turma;
import com.example.spudydev.spudy.infraestrutura.utils.Auxiliar;
import com.example.spudydev.spudy.entidades.professor.gui.MainProfessorActivity;

public class CriarTurmaActivity extends AppCompatActivity {

    private EditText nomeTurma;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_professor_criar_turma);

        nomeTurma = findViewById(R.id.edtNomeTurma);

        Button btnCriarTurma = findViewById(R.id.btnCriarTurma);
        btnCriarTurma.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Turma turma = new Turma();
                String codigoTurma = turma.criarTurma(nomeTurma.getText().toString());
                Auxiliar.criarToast(CriarTurmaActivity.this,"Codigo: " + codigoTurma);
                abrirTelaMainProfessor();
            }
        });

    }
    public void abrirTelaMainProfessor(){
        Intent abrirTelaMainProfessor = new Intent(CriarTurmaActivity.this, MainProfessorActivity.class);
        startActivity(abrirTelaMainProfessor);
        finish();
    }
}
