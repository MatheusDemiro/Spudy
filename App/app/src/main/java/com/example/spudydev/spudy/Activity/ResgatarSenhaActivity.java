package com.example.spudydev.spudy.Activity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.spudydev.spudy.R;

public class ResgatarSenhaActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_resgatar_senha);
    }
}
