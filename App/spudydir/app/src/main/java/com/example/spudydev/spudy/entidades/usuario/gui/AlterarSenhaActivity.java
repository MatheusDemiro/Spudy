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
import com.example.spudydev.spudy.infraestrutura.persistencia.AcessoFirebase;
import com.example.spudydev.spudy.infraestrutura.utils.Auxiliar;
import com.example.spudydev.spudy.perfil.gui.MeuPerfilAlunoActivity;
import com.example.spudydev.spudy.perfil.gui.MeuPerfilProfessorActivity;
import com.example.spudydev.spudy.infraestrutura.utils.VerificaConexao;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class AlterarSenhaActivity extends AppCompatActivity {

    private EditText edtAlterarSenhaAntiga;
    private EditText edtAlterarSenhaNova;
    private EditText edtAlterarSenhaNovaConfirma;
    private VerificaConexao verificaConexao;
    private boolean verifica;
    private String tipoConta;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alterar_senha);

        //Instatanciando os EditTexts
        edtAlterarSenhaAntiga = findViewById(R.id.edtAlterarSenhaAntiga);
        edtAlterarSenhaNova = findViewById(R.id.edtAlterarSenhaNova);
        edtAlterarSenhaNovaConfirma = findViewById(R.id.edtAlterarSenhaNovaConfirma);
        Button btn_alterarSenhaPerfil = findViewById(R.id.btnAlterarSenhaPerfil);
        verificaConexao = new VerificaConexao(this);
        tipoConta = getIntent().getStringExtra("tipoConta");

        btn_alterarSenhaPerfil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (verificaConexao.estaConectado()) {
                    if (verificaCampo()) {
                        alteraSenha();
                        Toast.makeText(AlterarSenhaActivity.this, "Senha alterada com sucesso.", Toast.LENGTH_SHORT).show();
                        abrirTelaMeuPerfil();
                    }
                }else{
                    Auxiliar.criarToast(getApplicationContext(), getString(R.string.sp_conexao_falha));
                }
            }
        });

    }

    public boolean verificaCampo() {

        boolean verificador = true;

        if (edtAlterarSenhaAntiga.getText().toString().isEmpty() ||
                edtAlterarSenhaAntiga.getText().toString().trim().length() == 0){
            edtAlterarSenhaAntiga.setError(getString(R.string.sp_excecao_senha));
            verificador = false;
        }
        if (edtAlterarSenhaNova.getText().toString().isEmpty() || edtAlterarSenhaNova.getText().toString().trim().length() == 0){
            edtAlterarSenhaNova.setError(getString(R.string.sp_excecao_senha));
            verificador = false;
        }
        if (edtAlterarSenhaNova.getText().toString().length() < 6){
            edtAlterarSenhaNova.setError(getString(R.string.sp_excecao_senha));
            verificador = false;
        }
        if (edtAlterarSenhaNovaConfirma.getText().toString().isEmpty() ||
                edtAlterarSenhaNovaConfirma.getText().toString().trim().length() == 0){
            edtAlterarSenhaNova.setError(getString(R.string.sp_excecao_campo_vazio));
            verificador = false;
        }
        if (!edtAlterarSenhaNova.getText().toString().equals(edtAlterarSenhaNovaConfirma.getText().toString())){
            edtAlterarSenhaNova.setError(getString(R.string.sp_excecao_senhas_iguais));
            edtAlterarSenhaNovaConfirma.setError(getString(R.string.sp_excecao_senhas_iguais));
            verificador = false;
        }
        return verificador;
    }
    private void alteraSenha() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user.getEmail() != null){
            AcessoFirebase.getFirebaseAutenticacao().signInWithEmailAndPassword(user.getEmail(), edtAlterarSenhaAntiga.getText().toString()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        FirebaseUser user = AcessoFirebase.getFirebaseAutenticacao().getCurrentUser();
                        if (user != null) {
                            user.updatePassword(edtAlterarSenhaNova.getText().toString());
                        }
                    }
                }
            });
        }
    }
    public void abrirTelaMeuPerfil (){
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
