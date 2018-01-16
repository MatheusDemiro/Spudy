package com.example.spudydev.spudy.pessoa.gui;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.spudydev.spudy.perfil.gui.MeuPerfilAlunoActivity;
import com.example.spudydev.spudy.infraestrutura.persistencia.AcessoFirebase;
import com.example.spudydev.spudy.R;
import com.example.spudydev.spudy.registro.negocio.VerificaConexao;
import com.github.rtoshiro.util.format.SimpleMaskFormatter;
import com.github.rtoshiro.util.format.text.MaskTextWatcher;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;

public class AlterarDataNascimentoActivity extends AppCompatActivity {
    private EditText edt_alterarDataNascimento;
    private VerificaConexao verificaConexao;
    private FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alterar_data_nascimento);

        edt_alterarDataNascimento = (EditText)findViewById(R.id.edt_AlterarDataNascimento);
        Button btn_alterarDataNascimento = (Button)findViewById(R.id.btn_AlterarDataNascimento);
        verificaConexao = new VerificaConexao(this);

        //Máscara
        edt_alterarDataNascimento = (EditText) findViewById(R.id.edt_AlterarDataNascimento);
        SimpleMaskFormatter smf = new SimpleMaskFormatter("NN/NN/NNNN");
        MaskTextWatcher mtw = new MaskTextWatcher(edt_alterarDataNascimento, smf);
        edt_alterarDataNascimento.addTextChangedListener(mtw);

        btn_alterarDataNascimento.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(verificaConexao.estaConectado()){
                    DatabaseReference referencia = AcessoFirebase.getFirebase().child("pessoa").child(user.getUid()).child("dataNascimento");
                    if (!edt_alterarDataNascimento.getText().toString().isEmpty()){
                        referencia.setValue(edt_alterarDataNascimento.getText().toString());
                        Toast.makeText(AlterarDataNascimentoActivity.this, R.string.sp_alterar_data_nascimento_sucesso, Toast.LENGTH_SHORT).show();
                        abrirTelaMeuPerfilActivity();
                    } else {
                        edt_alterarDataNascimento.setError(getString(R.string.sp_excecao_campo_vazio));
                    }

                }else{
                    Toast.makeText(AlterarDataNascimentoActivity.this, R.string.sp_conexao_falha, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    public void abrirTelaMeuPerfilActivity (){
        Intent intentAbrirTelaMeuPerfilActivity  = new Intent(this, MeuPerfilAlunoActivity.class);
        startActivity(intentAbrirTelaMeuPerfilActivity );
        finish();
    }
}
