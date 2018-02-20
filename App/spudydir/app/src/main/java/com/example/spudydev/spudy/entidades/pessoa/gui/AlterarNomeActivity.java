package com.example.spudydev.spudy.entidades.pessoa.gui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.spudydev.spudy.R;
import com.example.spudydev.spudy.entidades.pessoa.negocio.PessoaServices;
import com.example.spudydev.spudy.infraestrutura.utils.Auxiliar;
import com.example.spudydev.spudy.perfil.gui.MeuPerfilAlunoActivity;
import com.example.spudydev.spudy.perfil.gui.MeuPerfilProfessorActivity;
import com.example.spudydev.spudy.infraestrutura.utils.VerificaConexao;

/**
 * The type Alterar nome activity.
 */
public class AlterarNomeActivity extends AppCompatActivity {

    private EditText edtAlterarNome;
    private VerificaConexao verificaConexao;
    private String tipoConta;

    @Override
    protected final void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alterar_nome);

        edtAlterarNome = findViewById(R.id.edt_AlterarNome);
        Button btnAlterarNome = findViewById(R.id.btn_AlterarNome);

        verificaConexao = new VerificaConexao(this);
        tipoConta = getIntent().getStringExtra("tipoConta");

        btnAlterarNome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (verificaConexao.estaConectado()) {
                    if (verificaCampo()) {
                        alterarNome(edtAlterarNome.getText().toString());
                    }
                } else {
                    Auxiliar.criarToast(AlterarNomeActivity.this, getString(R.string.sp_conexao_falha));
                }
            }
        });
    }
    //Verificando o campo nome
    private boolean verificaCampo(){
        if (edtAlterarNome.getText().toString().isEmpty() || edtAlterarNome.getText().toString().trim().length() == 0){
            edtAlterarNome.setError(getString(R.string.sp_excecao_nome_invalido));
            return false;
        }
        return true;
    }
    //Método que chama a camada de negócio
    private void alterarNome(String novoNome) {
        if (PessoaServices.alterarNomeServices(novoNome)){
            Auxiliar.criarToast(AlterarNomeActivity.this, getString(R.string.sp_nome_alterado_sucesso));
            abrirTelaMeuPerfilActivity();
        }else {
            Auxiliar.criarToast(getApplicationContext(), getString(R.string.sp_excecao_database));
        }
    }
    //Intent para a tela de perfil do usário logado
    private void abrirTelaMeuPerfilActivity (){
        if (tipoConta.equals("aluno")) {
            Intent intent = new Intent(this, MeuPerfilAlunoActivity.class);
            startActivity(intent);
            finish();
        }else{
            Intent intent = new Intent(this, MeuPerfilProfessorActivity.class);
            startActivity(intent);
            finish();
        }
    }
}
