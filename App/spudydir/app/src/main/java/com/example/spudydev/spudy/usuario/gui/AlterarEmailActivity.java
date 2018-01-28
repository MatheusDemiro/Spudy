package com.example.spudydev.spudy.usuario.gui;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.spudydev.spudy.R;
import com.example.spudydev.spudy.infraestrutura.persistencia.AcessoFirebase;
import com.example.spudydev.spudy.infraestrutura.utils.VerificaConexao;
import com.example.spudydev.spudy.perfil.gui.MeuPerfilAlunoActivity;
import com.example.spudydev.spudy.perfil.gui.MeuPerfilProfessorActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;

public class AlterarEmailActivity extends AppCompatActivity {

    private EditText edt_alterarEmail;
    private EditText edt_alterarEmailSenha;
    private VerificaConexao verificaConexao;
    private String tipoConta;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alterar_email);

        edt_alterarEmail = (EditText) findViewById(R.id.edtAlterarEmailPerfil);
        edt_alterarEmailSenha = (EditText) findViewById(R.id.edtAlterarEmailSenhaPerfil);
        Button btn_alterarEmail = (Button) findViewById(R.id.btnAlterarEmailPerfil);
        verificaConexao = new VerificaConexao(this);
        tipoConta = getIntent().getStringExtra("tipoConta");

        btn_alterarEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (verificaConexao.estaConectado()) {
                    if (verificaCampos()) {
                        Toast.makeText(AlterarEmailActivity.this, "Email alterado com sucesso.", Toast.LENGTH_SHORT).show();
                        abrirTelaMeuPerfilActivity();
                    }
                }else{
                    Toast.makeText(getApplicationContext(), R.string.sp_conexao_falha, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    public boolean verificaCampos(){
        if (edt_alterarEmail.getText().toString().isEmpty()){
            edt_alterarEmail.setError(getString(R.string.sp_excecao_campo_vazio));
            return false;
        }
        if (edt_alterarEmailSenha.getText().toString().isEmpty()){
            edt_alterarEmailSenha.setError(getString(R.string.sp_excecao_campo_vazio));
            return false;
        }
        alteraEmail();
        return true;
    }
    public void alteraEmail(){
        AcessoFirebase.getFirebaseAutenticacao().signInWithEmailAndPassword(AcessoFirebase.getFirebaseAutenticacao().getCurrentUser().getEmail(),edt_alterarEmailSenha.getText().toString()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){
                    AcessoFirebase.getFirebaseAutenticacao().getCurrentUser().updateEmail(edt_alterarEmail.getText().toString());
                    AcessoFirebase.getFirebase().child("usuario").child(AcessoFirebase.getFirebaseAutenticacao().getCurrentUser().getUid()).child("email").setValue(edt_alterarEmail.getText().toString());
                }
            }
        });
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
