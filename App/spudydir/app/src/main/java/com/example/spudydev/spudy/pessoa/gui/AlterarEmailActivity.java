package com.example.spudydev.spudy.pessoa.gui;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.EditText;

import com.example.spudydev.spudy.R;
import com.example.spudydev.spudy.registro.negocio.VerificaConexao;
import com.google.firebase.auth.FirebaseUser;

public class AlterarEmailActivity extends AppCompatActivity {

    private EditText edt_alterarEmail;
    private EditText edt_alterarEmailSenha;
    private Button btn_alterarEmail;
    private VerificaConexao verificaConexao;
    private FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alterar_email);

        edt_alterarEmail = (EditText) findViewById(R.id.edt_AlterarEmailPerfil);
        edt_alterarEmailSenha = (EditText) findViewById(R.id.edt_AlterarEmailSenhaPerfil);
        btn_alterarEmail = (Button) findViewById(R.id.btn_AlterarEmailPerfil);
    }
}
