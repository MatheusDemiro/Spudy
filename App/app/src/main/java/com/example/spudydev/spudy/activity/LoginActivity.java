package com.example.spudydev.spudy.activity;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.spudydev.spudy.registro.gui.RegistroActivity;
import com.example.spudydev.spudy.usuario.aluno.gui.MainAlunoActivity;
import com.example.spudydev.spudy.banco.AcessoFirebase;
import com.example.spudydev.spudy.usuario.dominio.Usuario;
import com.example.spudydev.spudy.R;
import com.example.spudydev.spudy.registro.negocio.VerificaConexao;
import com.example.spudydev.spudy.usuario.professor.gui.MainProfessorActivity;
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
    private Button btn_logar;
    private TextView txtRegistreSe;
    private TextView txtEsqueciSenha;

    private Usuario usuario;
    private VerificaConexao verificaConexao;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_login);
        FirebaseAuth.getInstance().signOut();

        edtEmail = (EditText) findViewById(R.id.edt_email);
        edtSenha = (EditText) findViewById(R.id.edt_senha);
        btn_logar = (Button) findViewById(R.id.btn_entrar);

        verificaConexao = new VerificaConexao(this);

        btn_logar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (verificaConexao.estaConectado()) {
                    String email = edtEmail.getText().toString();
                    String senha = edtSenha.getText().toString();

                    usuario = new Usuario();
                    usuario.setEmail(email);
                    usuario.setSenha(senha);

                    if (validarCampos(usuario)) {
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
        txtRegistreSe = findViewById(R.id.txt_registre_se);
        txtEsqueciSenha = findViewById(R.id.txt_esqueci_senha);
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
    //realizando verificação de campos
    private boolean validarCampos(Usuario usuario){
        boolean validacao = true;

        if (usuario.getEmail() == null || usuario.getEmail().trim().length() == 0){
            edtEmail.setError(getString(R.string.sp_excecao_campo_vazio));
            validacao = false;
        }

        if (usuario.getSenha() == null || usuario.getSenha().trim().length() == 0){
            edtSenha.setError(getString(R.string.sp_excecao_campo_vazio));
            validacao = false;
        }
        return validacao;
    }

    //realizando a autenticação do usuário
    private void validarLogin(){

        FirebaseAuth autenticacao;

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