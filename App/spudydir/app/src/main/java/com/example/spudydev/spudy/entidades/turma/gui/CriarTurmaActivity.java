package com.example.spudydev.spudy.entidades.turma.gui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import com.example.spudydev.spudy.R;
import com.example.spudydev.spudy.entidades.turma.dominio.Turma;
import com.example.spudydev.spudy.entidades.turma.negocio.TurmaServices;
import com.example.spudydev.spudy.entidades.turma.persistência.TurmaDAO;
import com.example.spudydev.spudy.entidades.professor.gui.MainProfessorActivity;

public class CriarTurmaActivity extends AppCompatActivity {

    private EditText edtNomeTurma;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_professor_criar_turma);

        edtNomeTurma = findViewById(R.id.edtNomeTurma);

        Button btnCriarTurma = findViewById(R.id.btnCriarTurma);
        btnCriarTurma.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                salvarTurma();
            }
        });
    }
    private void salvarTurma() {
        Turma turma = new Turma();
        if (verificaCampo()) {
            turma.setNome(edtNomeTurma.getText().toString().toUpperCase());

            TurmaServices.criarTurmaProfessor(getApplicationContext(),turma);
        }
    }
    public boolean verificaCampo(){
        if (edtNomeTurma.getText().toString().isEmpty() || edtNomeTurma.getText().toString().trim().length() == 0){
            edtNomeTurma.setError(getString(R.string.sp_excecao_campo_vazio));
            return false;
        }
        return true;
    }
}
