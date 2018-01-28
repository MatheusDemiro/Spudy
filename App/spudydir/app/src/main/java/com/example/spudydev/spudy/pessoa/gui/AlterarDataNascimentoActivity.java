package com.example.spudydev.spudy.pessoa.gui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.spudydev.spudy.R;
import com.example.spudydev.spudy.infraestrutura.persistencia.AcessoFirebase;
import com.example.spudydev.spudy.infraestrutura.utils.Auxiliar;
import com.example.spudydev.spudy.perfil.gui.MeuPerfilAlunoActivity;
import com.example.spudydev.spudy.perfil.gui.MeuPerfilProfessorActivity;
import com.example.spudydev.spudy.infraestrutura.utils.VerificaConexao;
import com.github.rtoshiro.util.format.SimpleMaskFormatter;
import com.github.rtoshiro.util.format.text.MaskTextWatcher;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class AlterarDataNascimentoActivity extends AppCompatActivity {

    private EditText edtAlterarDataNascimento;
    private VerificaConexao verificaConexao;
    private FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    private String tipoConta;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alterar_data_nascimento);

        edtAlterarDataNascimento = (EditText) findViewById(R.id.edtAlterarDataNascimento);
        Button btn_alterarDataNascimento = (Button) findViewById(R.id.btnAlterarDataNascimento);
        verificaConexao = new VerificaConexao(this);
        tipoConta = getIntent().getStringExtra("tipoConta");

        //Máscara
        Auxiliar.criarMascara(edtAlterarDataNascimento);

        btn_alterarDataNascimento.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (verificaConexao.estaConectado()) {
                    if (verificaCampo()){
                        alterarDataNascimento();
                        Auxiliar.criarToast(AlterarDataNascimentoActivity.this, getString(R.string.sp_alterar_data_nascimento_sucesso));
                        abrirTelaMeuPerfilActivity();
                    }
                } else {
                    Toast.makeText(getApplicationContext(), R.string.sp_conexao_falha, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public boolean verificaCampo(){
        if (edtAlterarDataNascimento.getText().toString().isEmpty()){
            edtAlterarDataNascimento.setError(getString(R.string.sp_excecao_campo_vazio));
            return false;
        }
        return true;
    }

    private void alterarDataNascimento() {
        AcessoFirebase.getFirebase().child("pessoa").child(user.getUid()).child("dataNascimento").setValue(edtAlterarDataNascimento.getText().toString());
    }

    public void abrirTelaMeuPerfilActivity (){
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
