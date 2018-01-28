package com.example.spudydev.spudy.usuario.gui;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.example.spudydev.spudy.R;
import com.example.spudydev.spudy.infraestrutura.persistencia.AcessoFirebase;
import com.example.spudydev.spudy.infraestrutura.utils.Auxiliar;
import com.example.spudydev.spudy.pessoa.Endereco;
import com.example.spudydev.spudy.pessoa.dominio.Pessoa;
import com.example.spudydev.spudy.infraestrutura.utils.VerificaConexao;
import com.example.spudydev.spudy.usuario.aluno.dominio.Aluno;
import com.example.spudydev.spudy.usuario.dominio.Usuario;
import com.example.spudydev.spudy.usuario.professor.dominio.Professor;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;

public class RegistroActivity extends AppCompatActivity {

    private EditText nomeReg;
    private EditText dataNascimentoReg;
    private EditText enderecoReg;
    private EditText complementoReg;
    private EditText cidadeReg;
    private Spinner estadoReg;
    private EditText instituicaoReg;
    private Spinner tipodecontaReg;
    private EditText emailReg;
    private EditText senhaReg;
    private EditText confirmaSenhaReg;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro);

        nomeReg = findViewById(R.id.edtNomeRegistro);
        dataNascimentoReg = findViewById(R.id.edtDataNascimentoRegistro);
        //
        enderecoReg = findViewById(R.id.edtEnderecoRegistro);
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
        final VerificaConexao verificaConexao = new VerificaConexao(this);

        //criando máscara para data de nascimento
        Auxiliar.criarMascara(dataNascimentoReg);

        btnConfirmaReg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (verificaConexao.estaConectado()) {
                    if (validarCampos()) {
                        if (senhaReg.getText().toString().equals(confirmaSenhaReg.getText().toString())) {
                            cadastrarUsuario();
                        } else {
                            senhaReg.setError(getString(R.string.sp_excecao_senhas_iguais));
                            confirmaSenhaReg.setError(getString(R.string.sp_excecao_senhas_iguais));
                        }
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
        //ENDEREÇO

        if (enderecoReg.getText().toString().trim().isEmpty()){
            enderecoReg.setError(getString(R.string.sp_excecao_campo_vazio));
            validacao = false;
        }

        //COMPLEMENTO - (PODE FICAR VAZIO)

        //CIDADE

        if (cidadeReg.getText().toString().trim().isEmpty()){
            cidadeReg.setError(getString(R.string.sp_excecao_campo_vazio));
            validacao = false;
        }

        //SPINNER ESTADO

        //if (estadoReg.getSelectedItem().toString().equals("Selecione o seu estado")){
          //  Auxiliar.criarToast(getApplicationContext(), getString(R.string.sp_excecao_campo_vazio));
            //validacao = false;
        //}

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
                if (task.isSuccessful()) {
                    cadastrar();
                    Auxiliar.criarToast(getApplicationContext(), getString(R.string.sp_usuario_cadastrado));
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
                        Auxiliar.criarToast(RegistroActivity.this, getString(R.string.sp_excecao_cadastro));
                        e.printStackTrace();
                    }
                }
            }
        });

    }

    //Criando usuario
    public Usuario criaUsuario(){
        Usuario usuario = new Usuario();
        usuario.setEmail(emailReg.getText().toString());
        usuario.setInstituicao(instituicaoReg.getText().toString());
        usuario.setTipoConta(tipodecontaReg.getSelectedItem().toString());
        return  usuario;
    }

    //Criando pessoa
    public Pessoa criaPessoa(Usuario usuario){
        Pessoa pessoa = new Pessoa();
        pessoa.setNome(nomeReg.getText().toString());
        pessoa.setDataNascimento(dataNascimentoReg.getText().toString());
        return pessoa;
    }

    //Criando aluno
    public Aluno criaAluno(Pessoa pessoa){
        Aluno aluno = new Aluno();
        aluno.setPessoa(pessoa);
        return aluno;
    }

    //Criando Professor
    public Professor criaProfessor(Pessoa pessoa){
        Professor professor = new Professor();
        professor.setPessoa(pessoa);
        return professor;
    }

    //Criando Endereço
    public Endereco criaEndereco (){
        Endereco endereco = new Endereco();
        endereco.setEndereco(enderecoReg.getText().toString());
        endereco.setComplemento(complementoReg.getText().toString());
        endereco.setCidade(cidadeReg.getText().toString());
        endereco.setEstado((String) estadoReg.getSelectedItem());
        return endereco;
    }

    //Cadastrar usuario
    public void cadastrar(){
        Usuario usuario = criaUsuario();
        Pessoa pessoa = criaPessoa(usuario);
        Endereco endereco = criaEndereco();

        String uidUsuario = AcessoFirebase.getFirebaseAutenticacao().getCurrentUser().getUid();

        //Salvando Pessoa e usuario
        AcessoFirebase.getFirebase().child("usuario").child(uidUsuario).setValue(usuario);
        AcessoFirebase.getFirebase().child("pessoa").child(uidUsuario).setValue(pessoa);
        AcessoFirebase.getFirebase().child("endereco").child(uidUsuario).setValue(endereco);

        //Salvando Aluno OU Professor
        if (usuario.getTipoConta().equals("Aluno")){
            AcessoFirebase.getFirebase().child("aluno").child(uidUsuario).child("turmas").child("SENTINELA").setValue("0");
        }else{
            AcessoFirebase.getFirebase().child("professor").child(uidUsuario).child("turmasMinistradas").child("SENTINELA").setValue("0");
        }
    }

    public void abrirTelaLoginUsuario(){
        Intent intentAbrirTelaRegistroConfirm = new Intent(RegistroActivity.this, ConfirmRegistroActivity.class);
        startActivity(intentAbrirTelaRegistroConfirm);
        finish();
    }
}