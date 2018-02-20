package com.example.spudydev.spudy.infraestrutura.gui;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.spudydev.spudy.R;
import com.example.spudydev.spudy.entidades.aluno.gui.MainAlunoActivity;
import com.example.spudydev.spudy.entidades.professor.gui.MainProfessorActivity;
import com.example.spudydev.spudy.entidades.usuario.gui.RegistroActivity;
import com.example.spudydev.spudy.infraestrutura.persistencia.AcessoFirebase;
import com.example.spudydev.spudy.infraestrutura.utils.Auxiliar;
import com.example.spudydev.spudy.infraestrutura.utils.VerificaConexao;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

/**
 * The type Login activity.
 */
public class LoginActivity extends AppCompatActivity {

    private EditText edtEmail;
    private EditText edtSenha;
    private VerificaConexao verificaConexao;

    @Override
    protected final void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_login);

        edtEmail = findViewById(R.id.edtEmail);
        edtSenha = findViewById(R.id.edtSenha);
        Button btnLogar = findViewById(R.id.btnEntrar);

        verificaConexao = new VerificaConexao(this);

        btnLogar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (verificaConexao.estaConectado()) {
                    if (validarCampos(edtEmail.getText().toString(),edtSenha.getText().toString())) {
                        verificarAutenticacao(edtEmail.getText().toString(),edtSenha.getText().toString());
                    }
                } else {
                    Toast.makeText(LoginActivity.this, getString(R.string.sp_conexao_falha), Toast.LENGTH_SHORT).show();
                }
            }
        });

        //Transição tela login para registro
        TextView txtRegistreSe = (TextView) findViewById(R.id.txtRegistreSe);
        TextView txtEsqueciSenha = (TextView) findViewById(R.id.txtEsqueciSenha);
        //Click Events
        txtEsqueciSenha.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                abrirTelaResgatarSenha();
            }
        });
        txtRegistreSe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View k) {
                abrirTelaRegistro();
            }
        });
        //Transição tela login para registro fim
    }

    /**
     * Verificar autenticacao.
     *
     * @param email the email
     * @param senha the senha
     */
//Verificando se o usuário está autenticado
    public void verificarAutenticacao(String email, String senha){
        AcessoFirebase.getFirebaseAutenticacao().signInWithEmailAndPassword(email, senha).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){
                    FirebaseUser user = AcessoFirebase.getFirebaseAutenticacao().getCurrentUser();
                    if (user != null){
                        verificarTipoConta(user);
                    }else {
                        Auxiliar.criarToast(getApplicationContext(), getString(R.string.sp_usuario_nao_encontrado));
                    }
                }else{
                    Auxiliar.criarToast(getApplicationContext(), getString(R.string.sp_usuario_senha_errados));
                }

            }
        });
    }
    //Verificando o tipo de conta do usuário
    private void verificarTipoConta(FirebaseUser user) {
        AcessoFirebase.getFirebase().child("usuario").child(user.getUid()).child("tipoConta").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String tipoConta = dataSnapshot.getValue(String.class);
                if (dataSnapshot.getValue(String.class) != null){
                    if(tipoConta.equals("Aluno")) {
                        abrirTelaAluno();
                        Auxiliar.criarToast(getApplicationContext(),getString(R.string.sp_login_bem_sucedido));
                    } else {
                        abrirTelaProfessor();
                        Auxiliar.criarToast(getApplicationContext(), getString(R.string.sp_login_bem_sucedido));
                    }
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }
    //Realizando verificação de campos
    private boolean validarCampos(String campoEmail, String campoSenha){
        boolean validacao = true;

        if (campoEmail.trim().length() == 0){
            edtEmail.setError(getString(R.string.sp_excecao_campo_vazio));
            validacao = false;
        }

        if (campoSenha.trim().length() == 0){
            edtSenha.setError(getString(R.string.sp_excecao_campo_vazio));
            validacao = false;
        }
        return validacao;
    }
    //Intent para tela de registro
    private void abrirTelaRegistro(){
        Intent intentAbrirTelaRegistro = new Intent(LoginActivity.this, RegistroActivity.class);
        startActivity(intentAbrirTelaRegistro);
    }
    //Intent para a tela de resgatar senha
    private void abrirTelaResgatarSenha(){
        Intent intentAbrirTelaResgatarSenha = new Intent (LoginActivity.this, ResgatarSenhaActivity.class);
        startActivity(intentAbrirTelaResgatarSenha);
    }
    //Intent para a tela main de aluno
    private void abrirTelaAluno(){
        Intent intentAbrirTelaAluno = new Intent(getApplicationContext(), MainAlunoActivity.class);
        startActivity(intentAbrirTelaAluno);
    }
    //Intent para a tela main de professor
    private void abrirTelaProfessor() {
        Intent intentAbrirTelaProfessor = new Intent(getApplicationContext(), MainProfessorActivity.class);
        startActivity(intentAbrirTelaProfessor);
    }
}