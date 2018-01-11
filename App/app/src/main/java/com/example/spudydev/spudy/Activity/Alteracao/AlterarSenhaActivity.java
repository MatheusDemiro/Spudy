package com.example.spudydev.spudy.Activity.Alteracao;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.spudydev.spudy.Activity.MeuPerfilProfessorActivity;
import com.example.spudydev.spudy.R;
import com.example.spudydev.spudy.Verificador.VerificaConexao;
import com.example.spudydev.spudy.Verificador.VerificadorAlterarSenhaActivity;

public class AlterarSenhaActivity extends AppCompatActivity {

    private EditText edt_alterarSenhaAntiga;
    private EditText edt_alterarSenhaNova;
    private EditText edt_alterarSenhaNovaConfirma;
    private Button btn_alterarSenhaPerfil;
    private VerificadorAlterarSenhaActivity verificadorAlterarSenhaActivity;
    private VerificaConexao verificaConexao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alterar_senha);

        edt_alterarSenhaAntiga = (EditText) findViewById(R.id.edt_AlterarSenhaAntiga);
        edt_alterarSenhaNova = (EditText) findViewById(R.id.edt_AlterarSenhaNova);
        edt_alterarSenhaNovaConfirma = (EditText) findViewById(R.id.edt_AlterarSenhaNovaConfirma);
        btn_alterarSenhaPerfil = (Button) findViewById(R.id.btn_AlterarSenhaPerfil);
        verificadorAlterarSenhaActivity = new VerificadorAlterarSenhaActivity();
        verificaConexao = new VerificaConexao(this);

        //Pega Emaail da intent
        final String email = getIntent().getExtras().getString("email");

        btn_alterarSenhaPerfil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String verificador = verificadorAlterarSenhaActivity.Verificar(email, edt_alterarSenhaAntiga.getText().toString(), edt_alterarSenhaNova.getText().toString(), edt_alterarSenhaNovaConfirma.getText().toString());
                Toast.makeText(AlterarSenhaActivity.this, verificador, Toast.LENGTH_SHORT).show();
                abrirTelaMeuPerfilProfessorActivity();
            }
        });

    }

    public void abrirTelaMeuPerfilProfessorActivity(){
        Intent intentAbrirTelaMeuPerfilProfessorActicity = new Intent(AlterarSenhaActivity.this, MeuPerfilProfessorActivity.class);
        startActivity(intentAbrirTelaMeuPerfilProfessorActicity);
    }
}
