package com.example.spudydev.spudy.entidades.usuario.gui;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.example.spudydev.spudy.R;
import com.example.spudydev.spudy.entidades.pessoa.negocio.PessoaServices;
import com.example.spudydev.spudy.entidades.endereco.negocio.EnderecoServices;
import com.example.spudydev.spudy.entidades.usuario.negocio.UsuarioServices;
import com.example.spudydev.spudy.infraestrutura.persistencia.AcessoFirebase;
import com.example.spudydev.spudy.infraestrutura.utils.Auxiliar;
import com.example.spudydev.spudy.entidades.endereco.dominio.Endereco;
import com.example.spudydev.spudy.entidades.pessoa.dominio.Pessoa;
import com.example.spudydev.spudy.infraestrutura.utils.VerificaConexao;
import com.example.spudydev.spudy.entidades.usuario.dominio.Usuario;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;

/**
 * The type Registro activity.
 */
public class RegistroActivity extends AppCompatActivity {

    private EditText nomeReg;
    private EditText dataNascimentoReg;
    private EditText ruaReg;
    private EditText complementoReg;
    private EditText cidadeReg;
    private Spinner estadoReg;
    private EditText instituicaoReg;
    private Spinner tipodecontaReg;
    private EditText emailReg;
    private EditText senhaReg;
    private EditText confirmaSenhaReg;

    private VerificaConexao verificaConexao;

    @Override
    protected final void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro);

        nomeReg = findViewById(R.id.edtNomeRegistro);
        dataNascimentoReg = findViewById(R.id.edtDataNascimentoRegistro);
        //
        ruaReg = findViewById(R.id.edtRuaRegistro);
        complementoReg = findViewById(R.id.edtComplementoRegistro);
        cidadeReg = findViewById(R.id.edtCidadeRegistro);
        estadoReg = findViewById(R.id.spnEstadoRegistro);
        //
        emailReg = findViewById(R.id.edtEmailRegistro);
        instituicaoReg = findViewById(R.id.edtInstituicaoRegistro);
        tipodecontaReg = findViewById(R.id.spnTipoDeContaRegistro);
        Button btnConfirmaReg = findViewById(R.id.btnConfirmRegistro);
        senhaReg = findViewById(R.id.edtSenhaRegistro);
        confirmaSenhaReg = findViewById(R.id.edtConfirmaSenhaRegistro);
        dataNascimentoReg = findViewById(R.id.edtDataNascimentoRegistro);

        //Conexao
        verificaConexao = new VerificaConexao(this);

        //criando máscara para data de nascimento
        Auxiliar.criarMascara(dataNascimentoReg);

        btnConfirmaReg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (verificaConexao.estaConectado()) {
                    if (validarCampos()) {
                        cadastrarUsuario();
                    }
                } else {
                    Auxiliar.criarToast(getApplicationContext(), getString(R.string.sp_conexao_falha));
                }
            }
        });
    }
    private boolean validarCampos(){
        boolean validacao = true;
        //NOME
        if (nomeReg.getText().toString().trim().isEmpty()){
            nomeReg.setError(getString(R.string.sp_excecao_campo_vazio));
            validacao = false;
        }
        //DATA NASCIMENTO
        if (dataNascimentoReg.getText().toString().trim().isEmpty()){
            dataNascimentoReg.setError(getString(R.string.sp_excecao_campo_vazio));
            validacao = false;
        }
        //EMAIL
        if ((!Auxiliar.verificaExpressaoRegularEmail(emailReg.getText().toString())) ||
                emailReg.getText().toString().trim().length() == 0){
            emailReg.setError(getString(R.string.sp_excecao_email));
            validacao = false;
        }
        //INSTITUIÇÃO
        if (instituicaoReg.getText().toString().trim().isEmpty()){
            instituicaoReg.setError(getString(R.string.sp_excecao_campo_vazio));
            validacao = false;
        }
        //SPINNER TIPO CONTA
        if (tipodecontaReg.getSelectedItem().toString().equals("Selecione o tipo de conta")){
            Auxiliar.criarToast(getApplicationContext(), getString(R.string.sp_excecao_tipo_conta));
            validacao = false;
        }
        //SENHA REG
        if (senhaReg.getText().toString().isEmpty()){
            senhaReg.setError(getString(R.string.sp_excecao_senha));
            validacao = false;
        }
        //CONFIRMA SENHA
        if (confirmaSenhaReg.getText().toString().isEmpty()){
            confirmaSenhaReg.setError(getString(R.string.sp_excecao_campo_vazio));
            validacao = false;
        }
        if (!senhaReg.getText().toString().equals(confirmaSenhaReg.getText().toString())){
            senhaReg.setError(getString(R.string.sp_excecao_senhas_iguais));
            confirmaSenhaReg.setError(getString(R.string.sp_excecao_senhas_iguais));
            validacao = false;
        }
        return validacao;
    }
    private void cadastrarUsuario(){
        final FirebaseAuth autenticacao = AcessoFirebase.getFirebaseAutenticacao();
        autenticacao.createUserWithEmailAndPassword(
                emailReg.getText().toString(),
                senhaReg.getText().toString()
        ).addOnCompleteListener(RegistroActivity.this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (verificaAutenticacao(task)){
                    inserirUsuario();
                }
            }
        });
    }
    //Chamando método da camada de negócio
    private void inserirUsuario() {
        if (UsuarioServices.inserirUsuarioServices(criaUsuario())){
            inserirPessoa();
        } else {
            Auxiliar.criarToast(getApplicationContext(), getString(R.string.sp_excecao_database));
        }
    }//Chamando método da camada de negócio
    private void inserirPessoa(){
        if (PessoaServices.inserirPessoaServices(criaPessoa())){
            inserirEndereco();
        } else {
            Auxiliar.criarToast(getApplicationContext(), getString(R.string.sp_excecao_database));
        }
    }
    //Chamando método da camada de negócio
    private void inserirEndereco(){
        if (EnderecoServices.inserirEndereco(criaEndereco())){
            Auxiliar.criarToast(getApplicationContext(), getString(R.string.sp_usuario_cadastrado));
            abrirTelaLoginUsuario();
        }
    }
    //Verificando autenticação
    private boolean verificaAutenticacao(@NonNull Task<AuthResult> task) {
        boolean verificador = true;

        try{
            if (task.isSuccessful()) {
                verificador = true;
            }else {
                verificador = false;
                throw task.getException();
            }
        }catch (FirebaseAuthWeakPasswordException e){
            senhaReg.setError(getString(R.string.sp_excecao_senha));
            confirmaSenhaReg.setError(getString(R.string.sp_excecao_senha));
        }catch (FirebaseAuthInvalidCredentialsException e){
            emailReg.setError(getString(R.string.sp_excecao_email));
        }catch (FirebaseAuthUserCollisionException e){
            emailReg.setError(getString(R.string.sp_excecao_email_cadastrado));
        }catch (Exception e){
            Auxiliar.criarToast(RegistroActivity.this, getString(R.string.sp_excecao_cadastro));
        }
        return verificador;
    }
    //Criando usuario
    private Usuario criaUsuario(){
        Usuario usuario = new Usuario();
        usuario.setEmail(emailReg.getText().toString());
        usuario.setInstituicao(instituicaoReg.getText().toString());
        usuario.setTipoConta(tipodecontaReg.getSelectedItem().toString());
        return  usuario;
    }
    //Criando pessoa
    private Pessoa criaPessoa(){
        Pessoa pessoa = new Pessoa();
        pessoa.setNome(nomeReg.getText().toString());
        pessoa.setDataNascimento(dataNascimentoReg.getText().toString());

        return pessoa;
    }
    //Criando endereço e verificando se os campos estão preenchidos ou não
    private Endereco criaEndereco(){
        Endereco endereco = new Endereco();

        if (ruaReg.getText().toString().trim().length() == 0){
            endereco.setRua("ND");
        }else {
            endereco.setRua(ruaReg.getText().toString());
        }
        if (complementoReg.getText().toString().trim().length() == 0){
            endereco.setComplemento("ND");
        }else {
            endereco.setComplemento(complementoReg.getText().toString());
        }
        if (cidadeReg.getText().toString().trim().length() == 0){
            endereco.setCidade("ND");
        }else {
            endereco.setCidade(cidadeReg.getText().toString());
        }
        if (estadoReg.getSelectedItem().equals("Selecione o seu estado")) {
            endereco.setEstado("ND");
        }else {
            endereco.setEstado(estadoReg.getSelectedItem().toString());
        }
        return endereco;
    }
    //Intent abrir tela de confirmar registro
    private void abrirTelaLoginUsuario(){
        Intent intentAbrirTelaRegistroConfirm = new Intent(RegistroActivity.this, ConfirmRegistroActivity.class);
        startActivity(intentAbrirTelaRegistroConfirm);
        finish();
    }
}