package com.example.spudydev.spudy.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.spudydev.spudy.DAO.AcessoFirebase;
import com.example.spudydev.spudy.Entidades.Pessoa;
import com.example.spudydev.spudy.Entidades.Usuario;
import com.example.spudydev.spudy.Helper.Preferencias;
import com.example.spudydev.spudy.R;
import com.example.spudydev.spudy.Verificador.VerificaConexao;
import com.example.spudydev.spudy.Verificador.VerificaRegistro;
import com.github.rtoshiro.util.format.SimpleMaskFormatter;
import com.github.rtoshiro.util.format.text.MaskTextWatcher;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;

public class RegistroActivity extends AppCompatActivity {

    private EditText nomeReg;
    private EditText emailReg;
    private EditText dataNascimentoReg;
    private EditText instituicaoReg;
    private Spinner tipodecontaReg;
    //private Spinner perguntaSegur;
    private Button btnConfirmaReg;
    //private EditText respostaSeguranca;
    private EditText senhaReg;
    private EditText confirmaSenhaReg;
    //private Usuario e Pessoa
    private Usuario usuario;
    private Pessoa pessoa;
    //Variaveis para enviar dados autenticados do registro
    private FirebaseAuth autenticacao;
    //Verifica conexao
    private VerificaConexao verificaConexao;
    //Verifica
    private VerificaRegistro verificaRegistro;
    private String msgVerifica;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_registro);

        nomeReg = (EditText) findViewById(R.id.edt_nomeReg);
        emailReg = (EditText) findViewById(R.id.edt_emailReg);
        dataNascimentoReg = (EditText) findViewById(R.id.edt_dataReg);
        instituicaoReg = (EditText) findViewById(R.id.edt_instituicaoReg);
        tipodecontaReg = (Spinner) findViewById(R.id.spn_tipoDeConta);
        btnConfirmaReg = (Button) findViewById(R.id.btn_confirmRegistro);
        senhaReg = (EditText) findViewById(R.id.edt_senhaReg);
        confirmaSenhaReg = (EditText) findViewById(R.id.edt_confirmaSenha);
        //Conexao
        verificaConexao = new VerificaConexao(this);


        //criando máscara para data de nascimento
        dataNascimentoReg = (EditText) findViewById(R.id.edt_dataReg);
        SimpleMaskFormatter smf = new SimpleMaskFormatter("NN/NN/NNNN");
        MaskTextWatcher mtw = new MaskTextWatcher(dataNascimentoReg, smf);
        dataNascimentoReg.addTextChangedListener(mtw);

        btnConfirmaReg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
                NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
                boolean isConnected = activeNetwork != null && activeNetwork.isConnectedOrConnecting();

                String nome = nomeReg.getText().toString();
                String email = emailReg.getText().toString();
                String data = dataNascimentoReg.getText().toString();
                String instituicao = instituicaoReg.getText().toString();
                String confirmaSenhaR = confirmaSenhaReg.getText().toString();
                String senhaR = senhaReg.getText().toString();
                String tipoConta = tipodecontaReg.getSelectedItem().toString();

                if(verificaConexao.estaConectado()){
                    if (senhaR.equals(confirmaSenhaR)) {
                        usuario = new Usuario();
                        pessoa = new Pessoa();

                        pessoa.setNome(nome);
                        pessoa.setData_nascimento(data);
                        usuario.setEmail(email);
                        usuario.setInstituicao(instituicao);
                        usuario.setTipoConta(tipoConta);
                        usuario.setSenha(senhaR);

                        verificaRegistro = new VerificaRegistro(usuario, pessoa);
                        msgVerifica = verificaRegistro.Verificar();
                        if (msgVerifica.equals("sucesso")) {
                            cadastrarUsuario();
                        }else{
                            Toast.makeText(RegistroActivity.this, msgVerifica, Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(RegistroActivity.this, getString(R.string.sp_senhas_iguais), Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(RegistroActivity.this, getString(R.string.sp_conexao_falha), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void cadastrarUsuario(){
        autenticacao = AcessoFirebase.getFirebaseAutenticacao();
        autenticacao.createUserWithEmailAndPassword(
                usuario.getEmail(),
                usuario.getSenha()//Poderíamos substituir pelo edt_senha
        ).addOnCompleteListener(RegistroActivity.this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(RegistroActivity.this, getString(R.string.sp_usuario_cadastrado), Toast.LENGTH_LONG).show();
                    usuario.setId(autenticacao.getCurrentUser().getUid());
                    pessoa.setIdUsuario(autenticacao.getCurrentUser().getUid());//guardando o id do usuário em essoa
                    pessoa.setUsuario(usuario);


                    //salvando usuário
                    AcessoFirebase.getFirebase().child("usuario").child(autenticacao.getCurrentUser().getUid()).setValue(usuario.toMapUsuario());
                    AcessoFirebase.getFirebase().child("pessoa").child(autenticacao.getCurrentUser().getUid()).setValue(pessoa.toMapPessoa());


                    abrirTelaLoginUsuario();
                }else{
                    String erroExcecao = "";

                    try{
                        throw task.getException();
                    }catch (FirebaseAuthWeakPasswordException e){
                        senhaReg.setError(getString(R.string.sp_excecao_senha));
                        confirmaSenhaReg.setError(getString(R.string.sp_excecao_senha));
                    }catch (FirebaseAuthInvalidCredentialsException e){
                        emailReg.setError(getString(R.string.sp_excecao_email));
                    }catch (FirebaseAuthUserCollisionException e){
                        emailReg.setError(getString(R.string.sp_excecao_email_cadastrado));
                    }catch (Exception e){
                        erroExcecao = getString(R.string.sp_excecao_cadastro);
                        e.printStackTrace();
                    }
                    Toast.makeText(RegistroActivity.this, "Erro: "+ erroExcecao, Toast.LENGTH_LONG).show();
                }
            }
        });

    }
    public void abrirTelaLoginUsuario(){
        Intent intentAbrirTelaRegistroConfirm = new Intent(RegistroActivity.this, ConfirmRegistroActivity.class);
        startActivity(intentAbrirTelaRegistroConfirm);
        finish();
    }
}