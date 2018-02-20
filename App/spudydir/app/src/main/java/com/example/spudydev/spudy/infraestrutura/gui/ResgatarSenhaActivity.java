package com.example.spudydev.spudy.infraestrutura.gui;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.spudydev.spudy.R;
import com.example.spudydev.spudy.infraestrutura.utils.Auxiliar;
import com.example.spudydev.spudy.infraestrutura.utils.VerificaConexao;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

/**
 * The type Resgatar senha email activity.
 */
public class ResgatarSenhaActivity extends AppCompatActivity {

    private EditText edtEmailResgatarSenha;

    private VerificaConexao verificaConexao;

    @Override
    protected final void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_resgatar_senha);

        edtEmailResgatarSenha = (EditText) findViewById(R.id.edtEmailResgatarSenha);
        Button btnEnviarEmail = findViewById(R.id.btnResgatarSenha);

        //Instanciando a classe VerificaConexao
        verificaConexao = new VerificaConexao(this);

        btnEnviarEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (verificaConexao.estaConectado()) {
                    if (verificarCampo()) {
                        resgatarSenhaViaEmail(edtEmailResgatarSenha.getText().toString());
                    }
                } else {
                    Auxiliar.criarToast(getApplicationContext(), getString(R.string.sp_conexao_falha));
                }
            }
        });
    }
    //Método OnClick do botão btn_resgatar_senha
    private boolean verificarCampo(){
        boolean verificador = true;

        if (edtEmailResgatarSenha.getText().length() == 0){
            edtEmailResgatarSenha.setError(getString(R.string.sp_excecao_campo_vazio));
            verificador = false;
        }
        return verificador;
    }
    //Método para enviar senha ao email que solicitou nova senha.
    private void resgatarSenhaViaEmail(String email){
        //talvez tenha que instanciar
        FirebaseAuth.getInstance().sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()){
                    Toast.makeText(ResgatarSenhaActivity.this, R.string.sp_email_enviado_sucesso, Toast.LENGTH_SHORT).show();
                    abrirTelaLoginActivity();
                }else {
                    edtEmailResgatarSenha.setError(getString(R.string.sp_excecao_email));
                }
            }
        });
    }
    //Intent para a tela de login
    private void abrirTelaLoginActivity(){
        Intent intentAbrirTelaLoginActivity = new Intent(ResgatarSenhaActivity.this, LoginActivity.class);
        startActivity(intentAbrirTelaLoginActivity);
        finish();
    }
}