package com.example.spudydev.spudy.pessoa.gui;

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
import com.example.spudydev.spudy.pessoa.utils.SenhaException;
import com.example.spudydev.spudy.perfil.gui.MeuPerfilProfessorActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class AlterarSenhaActivity extends AppCompatActivity {

    private EditText edt_alterarSenhaAntiga;
    private EditText edt_alterarSenhaNova;
    private EditText edt_alterarSenhaNovaConfirma;
    private boolean verifica;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alterar_senha);

        //Instatanciando os EditTexts
        edt_alterarSenhaAntiga = findViewById(R.id.edtAlterarSenhaAntiga);
        edt_alterarSenhaNova = findViewById(R.id.edtAlterarSenhaNova);
        edt_alterarSenhaNovaConfirma = findViewById(R.id.edtAlterarSenhaNovaConfirma);
        Button btn_alterarSenhaPerfil = findViewById(R.id.btn_AlterarSenhaPerfil);

        btn_alterarSenhaPerfil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean verificador = validarAlteracaoSenha(edt_alterarSenhaAntiga.getText().toString(), edt_alterarSenhaNova.getText().toString(), edt_alterarSenhaNovaConfirma.getText().toString());
                try{
                    if (verificador){
                        Toast.makeText(AlterarSenhaActivity.this, "Senha alterada com sucesso", Toast.LENGTH_SHORT).show();
                        abrirTelaMeuPerfilProfessorActivity();
                    }
                } catch (SenhaException e){//Assim que o catch parar de capturar a transição de tela será completada
                    Toast.makeText(AlterarSenhaActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }

            }
        });

    }

    public boolean validarAlteracaoSenha(String senhaAntiga, final String senhaNova, final String senhaNovaConfirma) {

        if (senhaAntiga.isEmpty()){
            edt_alterarSenhaAntiga.setError(getString(R.string.sp_excecao_campo_vazio));
            return false;
        }

        if (senhaNova.isEmpty()){
            edt_alterarSenhaNova.setError(getString(R.string.sp_excecao_campo_vazio));
            return false;
        }

        if (senhaNovaConfirma.isEmpty()){
            edt_alterarSenhaNova.setError(getString(R.string.sp_excecao_campo_vazio));
            return false;
        }

        if (senhaNova.length() < 6){
            edt_alterarSenhaNova.setError(getString(R.string.sp_excecao_senha));
            return false;
        }
        if (!senhaNova.equals(senhaNovaConfirma)){
            edt_alterarSenhaNova.setError(getString(R.string.sp_senhas_iguais));
            edt_alterarSenhaNovaConfirma.setError(getString(R.string.sp_senhas_iguais));
            return false;
        }
        verifica = true;
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user.getEmail() != null){
            AcessoFirebase.getFirebaseAutenticacao().signInWithEmailAndPassword(user.getEmail(),senhaAntiga).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                            FirebaseUser user = AcessoFirebase.getFirebaseAutenticacao().getCurrentUser();
                            user.updatePassword(senhaNova);
                    }else{
                        verifica = false;
                    }
                }
            });
        }
        return verifica;
    }

    public void abrirTelaMeuPerfilProfessorActivity() throws SenhaException{
        Intent intentAbrirTelaMeuPerfilProfessorActicity = new Intent(AlterarSenhaActivity.this, MeuPerfilProfessorActivity.class);
        startActivity(intentAbrirTelaMeuPerfilProfessorActicity);
    }
}
