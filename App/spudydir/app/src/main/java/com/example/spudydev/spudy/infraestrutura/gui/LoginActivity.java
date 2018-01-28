package com.example.spudydev.spudy.infraestrutura.gui;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.spudydev.spudy.R;
import com.example.spudydev.spudy.infraestrutura.negocio.LoginServices;
import com.example.spudydev.spudy.usuario.gui.RegistroActivity;
import com.example.spudydev.spudy.usuario.gui.ResgatarSenhaEmailActivity;
import com.example.spudydev.spudy.infraestrutura.utils.VerificaConexao;

public class LoginActivity extends AppCompatActivity {

    private EditText edtEmail;
    private EditText edtSenha;
    private VerificaConexao verificaConexao;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_login);

        edtEmail = (EditText) findViewById(R.id.edtEmail);
        edtSenha = (EditText) findViewById(R.id.edtSenha);
        Button btn_logar = (Button) findViewById(R.id.btnEntrar);

        verificaConexao = new VerificaConexao(this);

        btn_logar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (verificaConexao.estaConectado()) {

                    if (validarCampos(edtEmail.getText().toString(),edtSenha.getText().toString())) {
                        LoginServices.emailSenha(edtEmail.getText().toString(),edtSenha.getText().toString(), getApplicationContext());
                    }
                } else {
                    Toast.makeText(LoginActivity.this, getString(R.string.sp_conexao_falha), Toast.LENGTH_SHORT).show();
                }
            }
        });

        //Transição tela login para registro
        TextView txtRegistreSe = (TextView) findViewById(R.id.txtRegistreSe);
        TextView txtEsqueciSenha = (TextView) findViewById(R.id.txtEsqueciSenha);
        //Click Events
        txtEsqueciSenha.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                abrirTelaResgatarSenha();
            }
        });
        txtRegistreSe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View k) {
                abrirTelaRegistro();
            }
        });
        //Transição tela login para registro fim

    }
    //Realizando verificação de campos
    private boolean validarCampos(String CampoEmail, String CampoSenha){
        boolean validacao = true;

        if (CampoEmail.trim().length() == 0){
            edtEmail.setError(getString(R.string.sp_excecao_campo_vazio));
            validacao = false;
        }

        if (CampoSenha.trim().length() == 0){
            edtSenha.setError(getString(R.string.sp_excecao_campo_vazio));
            validacao = false;
        }
        return validacao;
    }
    //Telas
    public void abrirTelaRegistro(){
        Intent intentAbrirTelaRegistro = new Intent(LoginActivity.this, RegistroActivity.class);
        startActivity(intentAbrirTelaRegistro);
    }
    public void abrirTelaResgatarSenha(){
        Intent intentAbrirTelaResgatarSenha = new Intent (LoginActivity.this, ResgatarSenhaEmailActivity.class);
        startActivity(intentAbrirTelaResgatarSenha);
    }

}