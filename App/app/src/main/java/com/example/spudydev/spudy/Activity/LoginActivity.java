package com.example.spudydev.spudy.Activity;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.spudydev.spudy.DAO.AcessoFirebase;
import com.example.spudydev.spudy.Entidades.Usuario;
import com.example.spudydev.spudy.R;
import com.example.spudydev.spudy.Verificador.VerificaConexao;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

public class LoginActivity extends AppCompatActivity {

    private EditText edtEmail;
    private EditText edtSenha;
    private Button  btnLogar;
    private TextView tvRegistro;
    private TextView tvSenha;

    private Usuario usuario;
    private VerificaConexao verificaConexao;
    private FirebaseAuth autenticacao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_login);
        FirebaseAuth.getInstance().signOut();
        //Parte Firebase Inicio

        edtEmail = (EditText) findViewById(R.id.edt_email);
        edtSenha = (EditText) findViewById(R.id.edt_senha);
        btnLogar = (Button) findViewById(R.id.btn_entrar);
        verificaConexao = new VerificaConexao(this);

        btnLogar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (verificaConexao.estaConectado()) {
                    if (!edtEmail.getText().toString().equals("") && !edtSenha.getText().toString().equals("")) {
                        usuario = new Usuario();
                        usuario.setEmail(edtEmail.getText().toString());
                        usuario.setSenha(edtSenha.getText().toString());
                        validarLogin();
                    } else {
                        Toast.makeText(LoginActivity.this, getString(R.string.sp_campos_email_senha), Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(LoginActivity.this, getString(R.string.sp_conexao_falha), Toast.LENGTH_SHORT).show();
                }

            }
        });

        //Parte Firebase Fim

        //Transição tela login para registro
        tvRegistro = (TextView) findViewById(R.id.text_registro);
        tvSenha = (TextView) findViewById(R.id.text_esqueciSenha);
        //Click Events
        tvSenha.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                abrirTelaResgatarSenha();
            }
        });
        tvRegistro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View k) {
                    abrirTelaRegistro();
                }
        });
        //Transição tela login para registro fim

    }

    private void validarLogin(){

        autenticacao = AcessoFirebase.getFirebaseAutenticacao();
        autenticacao.signInWithEmailAndPassword(usuario.getEmail(), usuario.getSenha()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){
                    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                    if (user != null){
                        DatabaseReference databaseReferenceUser = AcessoFirebase.getFirebase().child("usuario");
                        databaseReferenceUser.child(user.getUid()).child("tipoConta").addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                String tipoConta = dataSnapshot.getValue(String.class);
                                if(tipoConta.equals("Aluno")) {
                                    abrirTelaAluno();
                                    Toast.makeText(LoginActivity.this, R.string.sp_login_bem_sucedido, Toast.LENGTH_SHORT).show();
                                } else {
                                    abrirTelaProfessor();
                                    Toast.makeText(LoginActivity.this, R.string.sp_login_bem_sucedido, Toast.LENGTH_SHORT).show();
                                }
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });

                        }
                }else{
                    Toast.makeText(LoginActivity.this, getString(R.string.sp_usuario_senha_errados), Toast.LENGTH_SHORT).show();
                }

            }
        });
    }

    //Telas
    public void abrirTelaAluno(){
           //Iniciando tela do Aluno
           Intent intentAbrirTelaAluno = new Intent(LoginActivity.this, MainAlunoActivity.class);
           startActivity(intentAbrirTelaAluno);
    }
    public void abrirTelaProfessor() {
        //Iniciando tela do Professor
        Intent intentAbrirTelaProfessor = new Intent(LoginActivity.this, MainProfessorActivity.class);
        startActivity(intentAbrirTelaProfessor);
    }
    public void abrirTelaRegistro(){
        Intent intentAbrirTelaRegistro = new Intent(LoginActivity.this, RegistroActivity.class);
        startActivity(intentAbrirTelaRegistro);
    }
    public void abrirTelaResgatarSenha(){
        Intent intentAbrirTelaResgatarSenha = new Intent (LoginActivity.this, ResgatarSenhaEmailActivity.class);
        startActivity(intentAbrirTelaResgatarSenha);
    }

}