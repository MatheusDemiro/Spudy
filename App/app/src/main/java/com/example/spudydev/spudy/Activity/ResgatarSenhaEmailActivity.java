package com.example.spudydev.spudy.Activity;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.spudydev.spudy.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class ResgatarSenhaEmailActivity extends AppCompatActivity {

    private EditText edt_resgatar_senha;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_resgatar_senha);

        edt_resgatar_senha = (EditText) findViewById(R.id.edt_email_resgatar_senha);
    }
    //Método OnClick do botão btn_resgatar_senha
    public void enviarEmail(View view){
        if (edt_resgatar_senha.getText().length() == 0){
            edt_resgatar_senha.setError(getString(R.string.sp_excecao_campo_vazio));
        }else {
            resgatarSenhaViaEmail(edt_resgatar_senha.getText().toString());
        }
    }
    //Método para enviar senha ao email que solicitou nova senha.
    public void resgatarSenhaViaEmail(String email){
        //talvez tenha que instanciar
        FirebaseAuth.getInstance().sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()){
                    Toast.makeText(ResgatarSenhaEmailActivity.this, R.string.sp_email_enviado_sucesso, Toast.LENGTH_SHORT).show();
                    abrirTelaLoginActivity();
                }else {
                    edt_resgatar_senha.setError(getString(R.string.sp_excecao_email));
                }
            }
        });
    }
    public void abrirTelaLoginActivity(){
        Intent intentAbrirTelaLoginActivity = new Intent(ResgatarSenhaEmailActivity.this, LoginActivity.class);
        startActivity(intentAbrirTelaLoginActivity);
        finish();

    }
}