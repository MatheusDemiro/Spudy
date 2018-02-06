package com.example.spudydev.spudy.entidades.usuario.gui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.spudydev.spudy.R;
import com.example.spudydev.spudy.infraestrutura.persistencia.AcessoFirebase;
import com.example.spudydev.spudy.perfil.gui.MeuPerfilAlunoActivity;
import com.example.spudydev.spudy.perfil.gui.MeuPerfilProfessorActivity;
import com.example.spudydev.spudy.infraestrutura.utils.VerificaConexao;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class AlterarInstituicaoActivity extends AppCompatActivity {

    private EditText edt_alterarInstituicao;
    private VerificaConexao verificaConexao;
    private FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    private String tipoConta;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alterar_instituicao);

        edt_alterarInstituicao = findViewById(R.id.edtAlterarInstituicao);
        Button btn_alterarInstituicao = findViewById(R.id.btnAlterarInstituicao);
        verificaConexao = new VerificaConexao(this);
        tipoConta = getIntent().getStringExtra("tipoConta");

        btn_alterarInstituicao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (verificaConexao.estaConectado()) {
                    if (verificaCampo()) {
                        alterarInstituicao();
                        Toast.makeText(AlterarInstituicaoActivity.this, R.string.sp_alterar_instituicao_sucesso, Toast.LENGTH_SHORT).show();
                        abrirTelaMeuPerfilActivity();
                    }
                }else{
                    Toast.makeText(getApplicationContext(), R.string.sp_conexao_falha, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private boolean verificaCampo(){
        if (edt_alterarInstituicao.getText().toString().isEmpty()){
            edt_alterarInstituicao.setError(getString(R.string.sp_excecao_campo_vazio));
            return false;
        }
        return true;
    }
    private void alterarInstituicao() {
        AcessoFirebase.getFirebase().child("usuario").child(user.getUid()).child("instituicao").setValue(edt_alterarInstituicao.getText().toString());
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
