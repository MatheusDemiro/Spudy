package com.example.spudydev.spudy.Activity.Alteracao;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.spudydev.spudy.Activity.MeuPerfilAlunoActivity;
import com.example.spudydev.spudy.DAO.AcessoFirebase;
import com.example.spudydev.spudy.R;
import com.example.spudydev.spudy.Verificador.VerificaConexao;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;

public class AlterarNomeActivity extends AppCompatActivity {
    private EditText edt_alterarNome;
    private Button btn_alterarNome;
    private VerificaConexao verificaConexao;
    private FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alterar_nome);

        verificaConexao = new VerificaConexao(this);
        edt_alterarNome = (EditText) findViewById(R.id.edt_AlterarNome);
        btn_alterarNome = (Button) findViewById(R.id.btn_AlterarNome);

        //Intent intent = getIntent();
        //Bundle dados = intent.getExtras();
        final String email = getIntent().getExtras().getString("email");
        //String email = dados.getString("email");
        btn_alterarNome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (verificaConexao.estaConectado()) {
                    DatabaseReference referencia = AcessoFirebase.getFirebase().child("pessoa").child(user.getUid()).child("nome");
                    referencia.setValue(edt_alterarNome.getText().toString());
                    Toast.makeText(AlterarNomeActivity.this, "Nome alterado com sucesso.", Toast.LENGTH_SHORT).show();
                    abrirTelaMeuPerfilActivity();
                } else {
                    Toast.makeText(AlterarNomeActivity.this, R.string.sp_conexao_falha, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    public void abrirTelaMeuPerfilActivity (){
        Intent intentAbrirTelaMeuPerfilActivity  = new Intent(this, MeuPerfilAlunoActivity.class);
        startActivity(intentAbrirTelaMeuPerfilActivity );
        finish();
    }
}
