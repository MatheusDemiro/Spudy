package com.example.spudydev.spudy.Activity;
import android.R.*;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.spudydev.spudy.DAO.ConfiguracaoFirebase;
import com.example.spudydev.spudy.Entidades.Usuarios;
import com.example.spudydev.spudy.Helper.Base64Custom;
import com.example.spudydev.spudy.Helper.Preferencias;
import com.example.spudydev.spudy.R;
import com.github.rtoshiro.util.format.SimpleMaskFormatter;
import com.github.rtoshiro.util.format.text.MaskTextWatcher;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.auth.FirebaseUser;

public class RegistroActivity extends AppCompatActivity {

    private String[] tipoConta = new String[]{"Selecione o tipo de conta", "Aluno", "Professor"};
    private String[] perguntaSegurança = new String[]{"Selecione a pergunta secreta", "Nome do seu primeiro animal de estimação", "Nome do melhor amigo de infância", "Filme predileto"};

    private EditText nomeReg;
    private EditText emailReg;
    private EditText dataNascimentoReg;
    private EditText instituicaoReg;
    private Spinner tipodecontaReg;
    private Spinner perguntaSegur;
    private Button btnConfirmaReg;
    private EditText respostaSeguranca;
    private EditText senhaReg;
    private EditText confirmaSenhaReg;
    private Usuarios usuarios;
    //Variaveis para enviar dados autenticados do registro
    private FirebaseAuth autenticacao;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_registro);

        nomeReg = (EditText) findViewById(R.id.edt_nomeReg);
        emailReg = (EditText) findViewById(R.id.edt_emailReg);
        dataNascimentoReg = (EditText) findViewById(R.id.edt_dataReg);
        instituicaoReg = (EditText) findViewById(R.id.edt_instituicaoReg);
        tipodecontaReg = (Spinner) findViewById(R.id.spn_tipodeconta);
        perguntaSegur = (Spinner) findViewById(R.id.spn_perguntaSeguranca);
        respostaSeguranca = (EditText) findViewById(R.id.edt_resposta);
        btnConfirmaReg = (Button) findViewById(R.id.btn_confirmRegistro);
        senhaReg = (EditText) findViewById(R.id.edt_senhaReg);
        confirmaSenhaReg = (EditText) findViewById(R.id.edt_confirmaSenha);

        //criando máscara para data de nascimento
        dataNascimentoReg = (EditText) findViewById(R.id.edt_dataReg);
        SimpleMaskFormatter smf = new SimpleMaskFormatter("NN/NN/NNNN");
        MaskTextWatcher mtw = new MaskTextWatcher(dataNascimentoReg, smf);
        dataNascimentoReg.addTextChangedListener(mtw);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, layout.simple_spinner_dropdown_item, tipoConta);
        adapter.setDropDownViewResource(layout.simple_spinner_dropdown_item);
        final Spinner sp = (Spinner) findViewById(R.id.spn_tipodeconta);
        sp.setAdapter(adapter);

        ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(this, layout.simple_spinner_dropdown_item, perguntaSegurança);
        adapter2.setDropDownViewResource(layout.simple_spinner_dropdown_item);
        Spinner sp2 = (Spinner) findViewById(R.id.spn_perguntaSeguranca);
        sp2.setAdapter(adapter2);

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
                String respSeguranca = respostaSeguranca.getText().toString();
                String confirmaSenhaR = confirmaSenhaReg.getText().toString();
                String senhaR = senhaReg.getText().toString();
                String perguntaS = perguntaSegur.getSelectedItem().toString();
                String tipoConta = tipodecontaReg.getSelectedItem().toString();

                if (isConnected) {
                    if (nome.isEmpty() || email.isEmpty() || data.isEmpty() || instituicao.isEmpty()
                            || respSeguranca.isEmpty() || confirmaSenhaR.isEmpty() || senhaR.isEmpty()) {
                        Toast.makeText(RegistroActivity.this, getString(R.string.sp_campoVazio), Toast.LENGTH_SHORT).show();
                    } else {
                        if (senhaReg.getText().toString().equals(confirmaSenhaReg.getText().toString())) {
                            usuarios = new Usuarios();
                            usuarios.setNome(nome);
                            usuarios.setEmail(email);
                            usuarios.setDataNasc(data);
                            usuarios.setInstituicao(instituicao);
                            usuarios.setSenha(senhaR);
                            usuarios.setPerguntaSeguranca(perguntaS);
                            usuarios.setResposta(respSeguranca);
                            usuarios.setTipoConta(tipoConta);
                            cadastrarUsuario();
                        } else {
                            Toast.makeText(RegistroActivity.this, getString(R.string.sp_senhasIguais), Toast.LENGTH_SHORT).show();
                        }
                    }
                }

                else {
                    Toast.makeText(RegistroActivity.this, getString(R.string.sp_conexaoFalha), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    private void cadastrarUsuario(){
        autenticacao = ConfiguracaoFirebase.getFirebaseAutenticacao();
        autenticacao.createUserWithEmailAndPassword(
                usuarios.getEmail(),
                usuarios.getSenha()
        ).addOnCompleteListener(RegistroActivity.this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(RegistroActivity.this, getString(R.string.sp_usuarioCadastrado), Toast.LENGTH_LONG).show();

                    String identificadorUsuario = Base64Custom.codificarBase64(usuarios.getEmail());
                    FirebaseUser usuarioFirebase = task.getResult().getUser();
                    usuarios.setId(identificadorUsuario);
                    usuarios.salvar();

                    Preferencias preferencias = new Preferencias(RegistroActivity.this);
                    preferencias.salvarUsuarioPreferencias(identificadorUsuario, usuarios.getNome());

                    abrirLoginUsuario();
                }else{
                    String erroExcecao = "";

                    try{

                        throw task.getException();
                    }catch (FirebaseAuthWeakPasswordException e){
                        erroExcecao = getString(R.string.sp_excecaoSenha);
                    }catch (FirebaseAuthInvalidCredentialsException e){
                        erroExcecao = getString(R.string.sp_excecaoEmail);
                    }catch (FirebaseAuthUserCollisionException e){
                        erroExcecao = getString(R.string.sp_excecaoEmailCadastrado);
                    }catch (Exception e){
                        erroExcecao = getString(R.string.sp_excecaoCadastro);
                        e.printStackTrace();
                    }
                    Toast.makeText(RegistroActivity.this, "Erro: "+ erroExcecao, Toast.LENGTH_LONG).show();


                }
            }
        });

    }
    public void abrirLoginUsuario(){
        Intent intentAbrirTelaRegistroConfirm = new Intent(RegistroActivity.this, ConfirmRegistroActivity.class);
        startActivity(intentAbrirTelaRegistroConfirm);
        finish();
    }
}





