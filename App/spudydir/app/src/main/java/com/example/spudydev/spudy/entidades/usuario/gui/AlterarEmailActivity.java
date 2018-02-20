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
import com.example.spudydev.spudy.entidades.usuario.negocio.UsuarioServices;
import com.example.spudydev.spudy.infraestrutura.gui.LoginActivity;
import com.example.spudydev.spudy.infraestrutura.persistencia.AcessoFirebase;
import com.example.spudydev.spudy.infraestrutura.utils.Auxiliar;
import com.example.spudydev.spudy.infraestrutura.utils.VerificaConexao;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

/**
 * The type Alterar email activity.
 */
public class AlterarEmailActivity extends AppCompatActivity {

    private EditText edtAlterarEmail;
    private EditText edtAlterarEmailSenha;
    private VerificaConexao verificaConexao;

    @Override
    protected final void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alterar_email);

        edtAlterarEmail = findViewById(R.id.edtAlterarEmailPerfil);
        edtAlterarEmailSenha = findViewById(R.id.edtAlterarEmailSenhaPerfil);
        Button btnAlterarEmail = findViewById(R.id.btnAlterarEmailPerfil);

        verificaConexao = new VerificaConexao(this);

        btnAlterarEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (verificaConexao.estaConectado()) {
                    if (verificaCampos()) {
                        validarEmail(edtAlterarEmail.getText().toString());
                    }
                }else{
                    Toast.makeText(getApplicationContext(), R.string.sp_conexao_falha, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    //Verificando campos de email e senha
    private boolean verificaCampos(){
        boolean verificador = true;

        if (!Auxiliar.verificaExpressaoRegularEmail(edtAlterarEmail.getText().toString()) ||
                edtAlterarEmail.getText().toString().isEmpty()){
            edtAlterarEmail.setError(getString(R.string.sp_excecao_email));
            verificador = false;
        }
        if (edtAlterarEmailSenha.getText().toString().isEmpty()){
            edtAlterarEmailSenha.setError(getString(R.string.sp_excecao_campo_vazio));
            verificador = false;
        }
        return verificador;
    }
    //Validando email e chamando
    private void validarEmail(final String novoEmail){
        AcessoFirebase.getFirebaseAutenticacao().signInWithEmailAndPassword(AcessoFirebase.getFirebaseAutenticacao().getCurrentUser().getEmail(),
                edtAlterarEmailSenha.getText().toString()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){
                    alteraEmail(novoEmail);
                }else {
                    edtAlterarEmailSenha.setError("Senha inválida");
                }
            }
        });
    }

    /**
     * Altera email.
     *
     * @param novoEmail the novo email
     */
//Chamando camada de negócio
    public void alteraEmail(String novoEmail){
        if (UsuarioServices.alterarEmailServices(novoEmail)){
            Auxiliar.criarToast(getApplicationContext(), getString(R.string.sp_nome_alterado_sucesso));
            encerrarSessaoUsuario();
        }else {
            Auxiliar.criarToast(getApplicationContext(), getString(R.string.sp_excecao_database));
        }
    }
    //SignOut
    private void encerrarSessaoUsuario() {
        Intent intent = new Intent(this, LoginActivity.class);
        FirebaseAuth.getInstance().signOut();
        startActivity(intent);
        finish();
    }
}
