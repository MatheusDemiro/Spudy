package com.example.spudydev.spudy.pessoa.gui;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.spudydev.spudy.infraestrutura.persistencia.AcessoFirebase;
import com.example.spudydev.spudy.perfil.gui.MeuPerfilProfessorActivity;
import com.example.spudydev.spudy.R;
import com.example.spudydev.spudy.registro.negocio.VerificaConexao;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseUser;

public class AlterarSenhaActivity extends AppCompatActivity {

    private EditText edt_alterarSenhaAntiga;
    private EditText edt_alterarSenhaNova;
    private EditText edt_alterarSenhaNovaConfirma;
    private Button btn_alterarSenhaPerfil;
    private VerificaConexao verificaConexao;
    private String verifica;
    private String email = getIntent().getExtras().getString("email"); //Pega Email da intent

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alterar_senha);

        //Instatanciando os EditTexts

        edt_alterarSenhaNova = findViewById(R.id.edt_AlterarSenhaNova);
        edt_alterarSenhaNovaConfirma = findViewById(R.id.edt_AlterarSenhaNovaConfirma);
        btn_alterarSenhaPerfil = findViewById(R.id.btn_AlterarSenhaPerfil);

        verificaConexao = new VerificaConexao(this);

        btn_alterarSenhaPerfil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String verificador = validarAlteracaoSenha();
                Toast.makeText(AlterarSenhaActivity.this, verificador, Toast.LENGTH_SHORT).show();
                abrirTelaMeuPerfilProfessorActivity();
            }
        });

    }

    //Criando variáveis para as instâncias dos EditTexts
    String senhaAntiga = edt_alterarSenhaAntiga.getText().toString();
    final String senhaNova = edt_alterarSenhaNova.getText().toString();
    final String senhaNovaConfirma = edt_alterarSenhaNovaConfirma.getText().toString();

    public String validarAlteracaoSenha() {

        if (senhaAntiga.isEmpty()){
            return "O campo senha antiga está vazio.";
        }

        if (senhaNova.isEmpty()){
            return "O campo senha nova está vazio.";
        }

        if (senhaNovaConfirma.isEmpty()){
            return "O campo confirmar senha está vazio.";
        }
        //caso atualize a senha
        verifica = "Senha alterada com Sucesso.";
        AcessoFirebase.getFirebaseAutenticacao().signInWithEmailAndPassword(email,senhaAntiga).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    if (senhaNova.equals(senhaNovaConfirma)) {
                        FirebaseUser user = AcessoFirebase.getFirebaseAutenticacao().getCurrentUser();
                        user.updatePassword(senhaNova);
                    }else{
                        verifica = "Digite a mesma senha nos campos: Senha nova e Confirmar senha nova.";
                    }
                }else{
                    verifica = "A senha atual é incorreta.";
                }
            }
        });
        return verifica;
    }

    public void abrirTelaMeuPerfilProfessorActivity(){
        Intent intentAbrirTelaMeuPerfilProfessorActicity = new Intent(AlterarSenhaActivity.this, MeuPerfilProfessorActivity.class);
        startActivity(intentAbrirTelaMeuPerfilProfessorActicity);
    }
}
