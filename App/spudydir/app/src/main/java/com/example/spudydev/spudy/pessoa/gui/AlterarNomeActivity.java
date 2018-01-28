package com.example.spudydev.spudy.pessoa.gui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.spudydev.spudy.R;
import com.example.spudydev.spudy.infraestrutura.persistencia.AcessoFirebase;
import com.example.spudydev.spudy.infraestrutura.utils.Auxiliar;
import com.example.spudydev.spudy.perfil.gui.MeuPerfilAlunoActivity;
import com.example.spudydev.spudy.perfil.gui.MeuPerfilProfessorActivity;
import com.example.spudydev.spudy.infraestrutura.utils.VerificaConexao;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class AlterarNomeActivity extends AppCompatActivity {

    private EditText edtAlterarNome;
    private VerificaConexao verificaConexao;
    private FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    private String tipoConta;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alterar_nome);

        edtAlterarNome = (EditText) findViewById(R.id.edt_AlterarNome);
        Button btnAlterarNome = (Button) findViewById(R.id.btn_AlterarNome);
        verificaConexao = new VerificaConexao(this);
        tipoConta = getIntent().getStringExtra("tipoConta");

        btnAlterarNome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (verificaConexao.estaConectado()) {
                    if (verificaCampo()) {
                        alterarNome();
                        Auxiliar.criarToast(AlterarNomeActivity.this, getString(R.string.sp_nome_alterado_sucesso));
                        abrirTelaMeuPerfilActivity();
                    }
                } else {
                    Auxiliar.criarToast(AlterarNomeActivity.this, getString(R.string.sp_conexao_falha));
                }
            }
        });
    }
    private boolean verificaCampo(){
        if (edtAlterarNome.getText().toString().isEmpty() || edtAlterarNome.getText().toString().trim().length() == 0){
            edtAlterarNome.setError(getString(R.string.sp_excecao_nome_invalido));
            return false;
        }
        return true;
    }
    private void alterarNome() {
        AcessoFirebase.getFirebase().child("pessoa").child(user.getUid()).child("nome").setValue(edtAlterarNome.getText().toString());
    }

    public void abrirTelaMeuPerfilActivity (){
        if (tipoConta.equals("aluno")) {
            Intent intent = new Intent(this, MeuPerfilAlunoActivity.class);
            startActivity(intent);
            finish();
        }else{
            Intent intent = new Intent(this, MeuPerfilProfessorActivity.class);
            startActivity(intent);
            finish();
        }
    }
}
