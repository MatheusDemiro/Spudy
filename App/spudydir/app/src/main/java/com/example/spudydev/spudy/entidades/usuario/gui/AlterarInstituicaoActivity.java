package com.example.spudydev.spudy.entidades.usuario.gui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.spudydev.spudy.R;
import com.example.spudydev.spudy.entidades.usuario.negocio.UsuarioServices;
import com.example.spudydev.spudy.infraestrutura.utils.Auxiliar;
import com.example.spudydev.spudy.perfil.gui.MeuPerfilAlunoActivity;
import com.example.spudydev.spudy.perfil.gui.MeuPerfilProfessorActivity;
import com.example.spudydev.spudy.infraestrutura.utils.VerificaConexao;

/**
 * The type Alterar instituicao activity.
 */
public class AlterarInstituicaoActivity extends AppCompatActivity {

    private EditText edtAlterarInstituicao;
    private VerificaConexao verificaConexao;
    private String tipoConta;

    @Override
    protected final void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alterar_instituicao);

        edtAlterarInstituicao = findViewById(R.id.edtAlterarInstituicao);
        Button btnAlterarInstituicao = findViewById(R.id.btnAlterarInstituicao);
        verificaConexao = new VerificaConexao(this);
        tipoConta = getIntent().getStringExtra("tipoConta");

        btnAlterarInstituicao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (verificaConexao.estaConectado()) {
                    if (verificaCampo()) {
                        alterarInstituicao(edtAlterarInstituicao.getText().toString());
                    }
                }else{
                    Toast.makeText(getApplicationContext(), R.string.sp_conexao_falha, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    //Verificando campo instituição
    private boolean verificaCampo(){
        if (edtAlterarInstituicao.getText().toString().isEmpty()){
            edtAlterarInstituicao.setError(getString(R.string.sp_excecao_campo_vazio));
            return false;
        }
        return true;
    }
    //Chamando camada de negócio
    private void alterarInstituicao(String novaInstituicao) {
        if (UsuarioServices.alterarInstituicaoServices(novaInstituicao)){
            Auxiliar.criarToast(getApplicationContext(), getString(R.string.sp_alterar_instituicao_sucesso));
            abrirTelaMeuPerfilActivity();
        }else {
            Auxiliar.criarToast(getApplicationContext(), getString(R.string.sp_excecao_database));
        }
    }
    //Intent para a tela de perfil do usuário logado
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
