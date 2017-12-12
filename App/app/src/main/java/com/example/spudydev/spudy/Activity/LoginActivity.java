package com.example.spudydev.spudy.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.NetworkInfo;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.spudydev.spudy.DAO.ConfiguracaoFirebase;
import com.example.spudydev.spudy.Entidades.Usuarios;
import com.example.spudydev.spudy.Helper.Base64Custom;
import com.example.spudydev.spudy.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import android.net.ConnectivityManager;

public class LoginActivity extends AppCompatActivity {

    private EditText edtEmail;
    private EditText edtSenha;
    private Button  btnLogar;
    private TextView tvRegistro;
    private TextView tvSenha;
    private FirebaseAuth autenticacao;
    private Usuarios usuarios;
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

        btnLogar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
                NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
                boolean isConnected = activeNetwork != null && activeNetwork.isConnectedOrConnecting();
                if (isConnected) {
                    if (!edtEmail.getText().toString().equals("") && !edtSenha.getText().toString().equals("")) {
                        usuarios = new Usuarios();
                        usuarios.setEmail(edtEmail.getText().toString());
                        usuarios.setSenha(edtSenha.getText().toString());

                        validarLogin();
                    } else {
                        Toast.makeText(LoginActivity.this, getString(R.string.sp_camposEmailSenha), Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(LoginActivity.this, getString(R.string.sp_conexaoFalha), Toast.LENGTH_SHORT).show();
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

        autenticacao = ConfiguracaoFirebase.getFirebaseAutenticacao();
        autenticacao.signInWithEmailAndPassword(usuarios.getEmail(),usuarios.getSenha()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                if (task.isSuccessful()){
                    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                    if (user != null){

                        DatabaseReference databaseReferenceUser = FirebaseDatabase.getInstance().getReference().child("usuario");
                        String crip = user.getEmail();
                        String ident = Base64Custom.codificarBase64(crip);//Codificando o email para achar o id do usuário

                        databaseReferenceUser.child(ident).child("tipoConta").addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                String tipoAluno = "Aluno";
                                String tipoConta = dataSnapshot.getValue(String.class);

                                if(tipoAluno.equals(tipoConta)){
                                    abrirTelaAluno();
                                    Toast.makeText(LoginActivity.this, getString(R.string.sp_longinBemSucedido), Toast.LENGTH_LONG).show();
                                }else {
                                    abrirTelaProfessor();
                                    Toast.makeText(LoginActivity.this, getString(R.string.sp_emConstrucao), Toast.LENGTH_LONG).show();
                                }
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });

                        }
                }else{
                    Toast.makeText(LoginActivity.this, getString(R.string.sp_usuarioSenhaErrados), Toast.LENGTH_SHORT).show();
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
        Intent intentAbrirTelaProfessor = new Intent(LoginActivity.this, TelaTesteActivity.class);
        startActivity(intentAbrirTelaProfessor);
    }
    public void abrirTelaRegistro(){
        Intent intentAbrirTelaRegistro = new Intent(LoginActivity.this, RegistroActivity.class);
        startActivity(intentAbrirTelaRegistro);
    }
    public void abrirTelaResgatarSenha(){
        Intent intentAbrirTelaResgatarSenha = new Intent (LoginActivity.this, ResgatarSenhaActivity.class);
        startActivity(intentAbrirTelaResgatarSenha);
    }

}