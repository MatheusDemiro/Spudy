package com.example.spudydev.spudy.Activity.Alteracao;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.spudydev.spudy.R;
import com.example.spudydev.spudy.Verificador.VerificadorAlterarEmailActivity;

public class AlterarEmailActivity extends AppCompatActivity {

    private EditText edt_alterarEmail;
    private EditText edt_alterarEmailSenha;
    private Button btn_alterarEmail;
    private VerificadorAlterarEmailActivity verificadorAlterarEmailActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alterar_email);

        edt_alterarEmail = (EditText) findViewById(R.id.edt_AlterarEmailPerfil);
        edt_alterarEmailSenha = (EditText) findViewById(R.id.edt_AlterarEmailSenhaPerfil);
        btn_alterarEmail = (Button) findViewById(R.id.btn_AlterarEmailPerfil);

        final String email = getIntent().getExtras().getString("email");

        btn_alterarEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(AlterarEmailActivity.this, verificadorAlterarEmailActivity.Verificar(edt_alterarEmail.getText().toString(),edt_alterarEmailSenha.getText().toString()), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
