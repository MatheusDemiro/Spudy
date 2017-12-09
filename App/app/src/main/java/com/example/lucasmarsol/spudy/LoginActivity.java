package com.example.lucasmarsol.spudy;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class LoginActivity extends AppCompatActivity {

    private TextView tvRegistro;
    private TextView tvSenha;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        tvRegistro = (TextView) findViewById(R.id.text_registro);
        tvSenha = (TextView) findViewById(R.id.text_esqueciSenha);
        tvSenha.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, ResgatarSenhaActivity.class);
                startActivity(intent);
            }
        });
        tvRegistro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View k) {
                    Intent intent2 = new Intent(LoginActivity.this, RegistroActivity.class);
                    startActivity(intent2);
                }
        });

    }
}