package com.example.spudydev.spudy.entidades.pessoa.gui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.spudydev.spudy.R;
import com.example.spudydev.spudy.entidades.pessoa.negocio.PessoaServices;
import com.example.spudydev.spudy.infraestrutura.utils.Auxiliar;
import com.example.spudydev.spudy.perfil.gui.MeuPerfilAlunoActivity;
import com.example.spudydev.spudy.perfil.gui.MeuPerfilProfessorActivity;
import com.example.spudydev.spudy.infraestrutura.utils.VerificaConexao;

/**
 * The type Alterar data nascimento activity.
 */
public class AlterarDataNascimentoActivity extends AppCompatActivity {

    private EditText edtAlterarDataNascimento;
    private VerificaConexao verificaConexao;
    private String tipoConta;

    @Override
    protected final void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alterar_data_nascimento);

        edtAlterarDataNascimento = findViewById(R.id.edtAlterarDataNascimento);
        Button btnAlterarDataNascimento = findViewById(R.id.btnAlterarDataNascimento);
        verificaConexao = new VerificaConexao(this);
        tipoConta = getIntent().getStringExtra("tipoConta");

        //Máscara
        Auxiliar.criarMascara(edtAlterarDataNascimento);

        btnAlterarDataNascimento.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (verificaConexao.estaConectado()) {
                    if (verificaCampo()){
                        alterarDataNascimento(edtAlterarDataNascimento.getText().toString());
                    }
                } else {
                    Toast.makeText(getApplicationContext(), R.string.sp_conexao_falha, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    //Verificando campo data de nascimento
    private boolean verificaCampo(){
        if (edtAlterarDataNascimento.getText().toString().isEmpty()){
            edtAlterarDataNascimento.setError(getString(R.string.sp_excecao_campo_vazio));
            return false;
        }
        return true;
    }
    //Método que chama a camada de negócio
    private void alterarDataNascimento(String novaData) {
        if (PessoaServices.alterarDataNascimentoServices(novaData)){
            Auxiliar.criarToast(AlterarDataNascimentoActivity.this, getString(R.string.sp_alterar_data_nascimento_sucesso));
            abrirTelaMeuPerfilActivity();
        }else {
            Auxiliar.criarToast(getApplicationContext(), getString(R.string.sp_excecao_database));
        }
    }
    //Intent para o perfil do usuário logado
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
