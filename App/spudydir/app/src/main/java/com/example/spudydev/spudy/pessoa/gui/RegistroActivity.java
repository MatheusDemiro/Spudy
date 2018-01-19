package com.example.spudydev.spudy.pessoa.gui;
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
import com.example.spudydev.spudy.infraestrutura.negocio.AcessoFirebase;
import com.example.spudydev.spudy.infraestrutura.utils.VerificaConexao;
import com.example.spudydev.spudy.pessoa.dominio.Pessoa;
import com.example.spudydev.spudy.usuario.dominio.Usuario;
import com.github.rtoshiro.util.format.SimpleMaskFormatter;
import com.github.rtoshiro.util.format.text.MaskTextWatcher;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseException;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegistroActivity extends AppCompatActivity {

    private EditText nomeReg;
    private EditText emailReg;
    private EditText dataNascimentoReg;
    private EditText instituicaoReg;
    private Spinner tipodecontaReg;
    private EditText senhaReg;
    private EditText confirmaSenhaReg;
    private FirebaseAuth autenticacao;
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
                String dataNascimento = dataNascimentoReg.getText().toString();
                String instituicao = instituicaoReg.getText().toString();
                String confirmaSenha = confirmaSenhaReg.getText().toString();
                String senha = senhaReg.getText().toString();
                String tipoConta = tipodecontaReg.getSelectedItem().toString();

                if(verificaConexao.estaConectado()){
                    if (senha.equals(confirmaSenha)) {
                        Usuario usuario = new Usuario();
                        Pessoa pessoa = new Pessoa();

                        usuario.setEmail(email);
                        usuario.setInstituicao(instituicao);
                        usuario.setTipoConta(tipoConta);
                        usuario.setSenha(senha);

                        pessoa.setNome(nome);
                        pessoa.setDataNascimento(dataNascimento);
                        pessoa.setUsuario(usuario);

                        if (validarCampos(pessoa)) {
                            autenticaUsuario(pessoa);
                        }
                    } else {
                        senhaReg.setError(getString(R.string.sp_senhas_iguais));
                        confirmaSenhaReg.setError(getString(R.string.sp_senhas_iguais));
                    }
                } else {
                    Toast.makeText(RegistroActivity.this, getString(R.string.sp_conexao_falha), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    //pattern para o email
    private boolean aplicarPadraoEmail(String email) {
        String excecoes = "^[A-Z0-9._%-]+@[A-Z0-9.-]+\\.[A-Z]{2,4}$";
        Pattern pattern = Pattern.compile(excecoes);
        Matcher matcher = pattern.matcher(email);

        return matcher.matches();
    }
    //validando campos
    private boolean validarCampos(Pessoa pessoa){
        boolean validacao = true;

        if (pessoa.getNome() == null || pessoa.getNome().trim().length() == 0){
            nomeReg.setError(getString(R.string.sp_excecao_nome_invalido));
            validacao = false;
        }

        if (!aplicarPadraoEmail(pessoa.getUsuario().getEmail().toUpperCase())
                || pessoa.getUsuario().getEmail().trim().length() == 0){
            emailReg.setError(getString(R.string.sp_excecao_email));
            validacao = false;
        }
        //validando data nascimento
        if (pessoa.getDataNascimento() == null || pessoa.getDataNascimento().length() < 10){
            dataNascimentoReg.setError(getString(R.string.sp_excecao_data_nascimento));
            validacao = false;
        }
        //validando instituicao
        if (pessoa.getUsuario().getInstituicao() == null || pessoa.getUsuario().getInstituicao().trim().length() == 0){
            instituicaoReg.setError(getString(R.string.sp_excecao_instituicao));
            validacao = false;
        }
        //validando tipoConta
        if (tipodecontaReg.getSelectedItem().equals("Selecione o tipo de conta")){
            Toast.makeText(RegistroActivity.this, R.string.sp_excecao_tipo_conta, Toast.LENGTH_SHORT).show();
            validacao = false;
        }
        //validando senha
        if (pessoa.getUsuario().getSenha().length() < 6 || pessoa.getUsuario().getSenha() == null){
            senhaReg.setError(getString(R.string.sp_excecao_senha));
            validacao = false;
        }
        //validando confirmar senha
        if (confirmaSenhaReg.getText().toString().length() == 0 || confirmaSenhaReg.getText().toString().length() < 6){
            confirmaSenhaReg.setError(getString(R.string.sp_excecao_senha));
            validacao = false;
        }

        return validacao;
    }
    //autentica e cadastrar usuário
    private void autenticaUsuario(final Pessoa pessoa){
        autenticacao = AcessoFirebase.getFirebaseAutenticacao();
        autenticacao.createUserWithEmailAndPassword(
                pessoa.getUsuario().getEmail(),
                pessoa.getUsuario().getSenha()
        ).addOnCompleteListener(RegistroActivity.this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    cadastrarUsuario(pessoa);
                }else{
                    try{
                        throw task.getException();
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

    private void cadastrarUsuario(Pessoa pessoa){
        FirebaseUser user = autenticacao.getCurrentUser();
        try{
            //salvando usuário
            if (user != null){
                AcessoFirebase.getFirebase().child("usuario").child(user.getUid()).setValue(pessoa.getUsuario().toMapUsuario());
                AcessoFirebase.getFirebase().child("pessoa").child(user.getUid()).setValue(pessoa.toMapPessoa());
                Toast.makeText(RegistroActivity.this, getString(R.string.sp_usuario_cadastrado), Toast.LENGTH_LONG).show();
                abrirTelaLoginUsuario();
            }
        } catch (DatabaseException e){
            Toast.makeText(RegistroActivity.this, getString(R.string.sp_excecao_banco), Toast.LENGTH_LONG).show();
        }

    }
    public void abrirTelaLoginUsuario(){
        Intent intentAbrirTelaRegistroConfirm = new Intent(RegistroActivity.this, ConfirmRegistroActivity.class);
        startActivity(intentAbrirTelaRegistroConfirm);
        finish();
    }
}