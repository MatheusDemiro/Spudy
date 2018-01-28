package com.example.spudydev.spudy.usuario.gui;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.spudydev.spudy.R;
import com.example.spudydev.spudy.infraestrutura.gui.LoginActivity;

public class ConfirmRegistroActivity extends AppCompatActivity {
    private Button button_OK;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_confirm_registro);

        button_OK = (Button) findViewById(R.id.btn_ok_confirmacao_registro);
        button_OK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent itet = new Intent(ConfirmRegistroActivity.this, LoginActivity.class);
                startActivity(itet);
            }
        });
    }
}
