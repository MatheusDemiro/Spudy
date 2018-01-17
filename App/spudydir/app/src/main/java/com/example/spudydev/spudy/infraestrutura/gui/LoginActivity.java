package com.example.spudydev.spudy.infraestrutura.gui;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.spudydev.spudy.pessoa.gui.ResgatarSenhaEmailActivity;
import com.example.spudydev.spudy.pessoa.gui.RegistroActivity;
import com.example.spudydev.spudy.usuario.gui.MainAlunoActivity;
import com.example.spudydev.spudy.infraestrutura.persistencia.AcessoFirebase;
import com.example.spudydev.spudy.usuario.dominio.Usuario;
import com.example.spudydev.spudy.infraestrutura.utils.UsuarioException;
import com.example.spudydev.spudy.R;
import com.example.spudydev.spudy.infraestrutura.utils.VerificaConexao;
import com.example.spudydev.spudy.usuario.gui.MainProfessorActivity;
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
    private VerificaConexao verificaConexao;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_login);
        FirebaseAuth.getInstance().signOut();

        edtEmail =  findViewById(R.id.edtEmail);
        edtSenha =  findViewById(R.id.edtSenha);
        Button btn_logar = findViewById(R.id.btnEntrar);

        verificaConexao = new VerificaConexao(this);

        btn_logar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (verificaConexao.estaConectado()) {
                    String email = edtEmail.getText().toString();
                    String senha = edtSenha.getText().toString();

                    Usuario usuario = new Usuario();
                    usuario.setEmail(email);
                    usuario.setSenha(senha);
                    try {
                        if (validarCampos(usuario)) {
                            validarLogin(usuario);
                        }
                    } catch (UsuarioException e){
                        Toast.makeText(LoginActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }

                } else {
                    Toast.makeText(LoginActivity.this, getString(R.string.sp_conexao_falha), Toast.LENGTH_SHORT).show();
                }

            }
        });

        //Transição tela login para registro
        TextView txtRegistreSe =  findViewById(R.id.txtRegistreSe);
        TextView txtEsqueciSenha =  findViewById(R.id.txtEsqueciSenha);
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
            edtEmail.setError(getString(R.string.sp_excecao_email));
            validacao = false;
        }

        if (usuario.getSenha() == null || usuario.getSenha().trim().length() == 0){
            edtSenha.setError(getString(R.string.sp_excecao_senha));
            validacao = false;
        }
        return validacao;
    }

    //realizando a autenticação do usuário
    private void validarLogin(Usuario usuario) throws UsuarioException{

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
                                if (tipoConta != null){
                                    if(tipoConta.equals("Aluno")) {
                                        abrirTelaAluno();
                                        Toast.makeText(LoginActivity.this, R.string.sp_login_bem_sucedido, Toast.LENGTH_SHORT).show();
                                    } else {
                                        abrirTelaProfessor();
                                        Toast.makeText(LoginActivity.this, R.string.sp_login_bem_sucedido, Toast.LENGTH_SHORT).show();
                                    }
                                }
                            }
                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });
                        }else {
                        Toast.makeText(LoginActivity.this, "Usuario não encontrado", Toast.LENGTH_SHORT).show();
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