package com.example.spudydev.spudy.entidades.usuario.gui;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.spudydev.spudy.R;
import com.example.spudydev.spudy.infraestrutura.gui.LoginActivity;
import com.example.spudydev.spudy.infraestrutura.persistencia.AcessoFirebase;
import com.example.spudydev.spudy.infraestrutura.utils.Auxiliar;
import com.example.spudydev.spudy.infraestrutura.utils.VerificaConexao;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class AlterarEmailActivity extends AppCompatActivity {

    private EditText edt_alterarEmail;
    private EditText edt_alterarEmailSenha;
    private VerificaConexao verificaConexao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alterar_email);

        edt_alterarEmail = findViewById(R.id.edtAlterarEmailPerfil);
        edt_alterarEmailSenha = findViewById(R.id.edtAlterarEmailSenhaPerfil);
        Button btn_alterarEmail = findViewById(R.id.btnAlterarEmailPerfil);
        verificaConexao = new VerificaConexao(this);

        btn_alterarEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (verificaConexao.estaConectado()) {
                    if (verificaCampos()) {
                        alteraEmail();
                    }
                }else{
                    Toast.makeText(getApplicationContext(), R.string.sp_conexao_falha, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    public boolean verificaCampos(){
        boolean verificador = true;

        if (!Auxiliar.verificaExpressaoRegularEmail(edt_alterarEmail.getText().toString()) ||
                edt_alterarEmail.getText().toString().isEmpty()){
            edt_alterarEmail.setError(getString(R.string.sp_excecao_email));
            verificador = false;
        }
        if (edt_alterarEmailSenha.getText().toString().isEmpty()){
            edt_alterarEmailSenha.setError(getString(R.string.sp_excecao_campo_vazio));
            verificador = false;
        }
        return verificador;
    }
    public void alteraEmail(){
        AcessoFirebase.getFirebaseAutenticacao().signInWithEmailAndPassword(AcessoFirebase.getFirebaseAutenticacao().getCurrentUser().getEmail(),edt_alterarEmailSenha.getText().toString()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){
                    AcessoFirebase.getFirebaseAutenticacao().getCurrentUser().updateEmail(edt_alterarEmail.getText().toString());
                    AcessoFirebase.getFirebase().child("usuario").child(AcessoFirebase.getFirebaseAutenticacao().getCurrentUser().getUid()).child("email").setValue(edt_alterarEmail.getText().toString());
                    Auxiliar.criarToast(getApplicationContext(),getString(R.string.sp_email_alterado_sucesso));
                    encerrarSessaoUsuario();
                }else {
                    edt_alterarEmailSenha.setError("Senha inválida");
                }
            }
        });
    }
    //SignOut
    public void encerrarSessaoUsuario() {
        Intent intent = new Intent(this, LoginActivity.class);
        FirebaseAuth.getInstance().signOut();
        startActivity(intent);
        finish();
    }
}
