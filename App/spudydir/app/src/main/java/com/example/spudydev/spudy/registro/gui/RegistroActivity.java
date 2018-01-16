package com.example.spudydev.spudy.registro.gui;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.spudydev.spudy.R;
import com.example.spudydev.spudy.infraestrutura.persistencia.AcessoFirebase;
import com.example.spudydev.spudy.infraestrutura.utils.UsuarioException;
import com.example.spudydev.spudy.pessoa.dominio.Pessoa;
import com.example.spudydev.spudy.registro.negocio.VerificaConexao;
import com.example.spudydev.spudy.usuario.dominio.Usuario;
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
    //private EditText respostaSeguranca;
    private EditText senhaReg;
    private EditText confirmaSenhaReg;
    //Variaveis para enviar dados autenticados do registro
    private FirebaseAuth autenticacao;
    //Verifica conexao
    private VerificaConexao verificaConexao;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro);

        nomeReg = findViewById(R.id.edtNomeRegistro);
        emailReg = findViewById(R.id.edtEmailRegistro);
        dataNascimentoReg = findViewById(R.id.edtDataRegistro);
        instituicaoReg = findViewById(R.id.edtInstituicaoRegistro);
        tipodecontaReg = findViewById(R.id.spnTipoDeConta);
        Button btnConfirmaReg = findViewById(R.id.btnConfirmRegistro);
        senhaReg = findViewById(R.id.edtSenhaReg);
        confirmaSenhaReg = findViewById(R.id.edtConfirmaSenha);
        //Conexao
        verificaConexao = new VerificaConexao(this);


        //criando máscara para data de nascimento
        dataNascimentoReg = findViewById(R.id.edtDataRegistro);
        SimpleMaskFormatter smf = new SimpleMaskFormatter("NN/NN/NNNN");
        MaskTextWatcher mtw = new MaskTextWatcher(dataNascimentoReg, smf);
        dataNascimentoReg.addTextChangedListener(mtw);

        btnConfirmaReg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String nome = nomeReg.getText().toString();
                String email = emailReg.getText().toString();
                String data = dataNascimentoReg.getText().toString();
                String instituicao = instituicaoReg.getText().toString();
                String confirmaSenhaR = confirmaSenhaReg.getText().toString();
                String senhaR = senhaReg.getText().toString();
                String tipoConta = tipodecontaReg.getSelectedItem().toString();

                if(verificaConexao.estaConectado()){
                    if (senhaR.equals(confirmaSenhaR)) {
                        Usuario usuario = new Usuario();
                        Pessoa pessoa = new Pessoa();

                        pessoa.setNome(nome);
                        pessoa.setDataNascimento(data);
                        usuario.setEmail(email);
                        usuario.setInstituicao(instituicao);
                        usuario.setTipoConta(tipoConta);
                        usuario.setSenha(senhaR);
                        try{
                            if (validarCampos(usuario, pessoa)){
                                cadastrarUsuario(usuario, pessoa);
                            }
                        } catch (UsuarioException e){
                            Toast.makeText(RegistroActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
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
    private boolean validarCampos(Usuario usuario, Pessoa pessoa){
        boolean validacao = true;

        if (pessoa.getNome() == null || pessoa.getNome().trim().length() == 0){
            nomeReg.setError(getString(R.string.sp_excecao_campo_vazio));
            validacao = false;
        }

        if (pessoa.getDataNascimento() == null || pessoa.getDataNascimento().trim().length() == 0){
            dataNascimentoReg.setError(getString(R.string.sp_excecao_campo_vazio));
            validacao = false;
        }

        if (usuario.getInstituicao() == null || usuario.getInstituicao().trim().length() == 0){
            instituicaoReg.setError(getString(R.string.sp_excecao_campo_vazio));
            validacao = false;
        }

        if (usuario.getInstituicao().equals("Selecione o tipo de conta")){
            instituicaoReg.setError(getString(R.string.sp_excecao_tipo_conta));
            validacao = false;
        }

        if (usuario.getEmail() == null || usuario.getEmail().trim().length() == 0){
            emailReg.setError(getString(R.string.sp_excecao_campo_vazio));
            validacao = false;
        }

        if (usuario.getSenha() == null || usuario.getSenha().trim().length() == 0){
            senhaReg.setError(getString(R.string.sp_excecao_campo_vazio));
            validacao = false;
        }

        if (confirmaSenhaReg.getText().toString().isEmpty()){
            confirmaSenhaReg.setError(getString(R.string.sp_excecao_campo_vazio));
            return false;
        }

        if (tipodecontaReg.getSelectedItem().equals("Selecione o tipo de conta")){
            confirmaSenhaReg.setError(getString(R.string.sp_excecao_tipo_conta));
            return false;
        }

        return validacao;
    }

    private void cadastrarUsuario(final Usuario usuario, final Pessoa pessoa) throws UsuarioException{
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
                    pessoa.setUsuario(usuario);
                    //salvando usuário
                    AcessoFirebase.getFirebase().child("usuario").child(autenticacao.getCurrentUser().getUid()).setValue(usuario.toMapUsuario());
                    AcessoFirebase.getFirebase().child("pessoa").child(autenticacao.getCurrentUser().getUid()).setValue(pessoa.toMapPessoa());

                    abrirTelaLoginUsuario();
                }else{
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
                        Toast.makeText(RegistroActivity.this, R.string.sp_excecao_cadastro, Toast.LENGTH_SHORT).show();
                        e.printStackTrace();
                    }
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