package com.example.spudydev.spudy.entidades.usuario.gui;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.spudydev.spudy.R;
import com.example.spudydev.spudy.infraestrutura.gui.LoginActivity;

/**
 * The type Confirm registro activity.
 */
public class ConfirmRegistroActivity extends AppCompatActivity {

    @Override
    protected final void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_confirm_registro);

        Button btnOk = findViewById(R.id.btn_ok_confirmacao_registro);
        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                abrirTelaLoginActivity();
            }
        });
    }
    //Intent para a tela de login
    private void abrirTelaLoginActivity(){
        Intent intent = new Intent(ConfirmRegistroActivity.this, LoginActivity.class);
        startActivity(intent);
    }
}
