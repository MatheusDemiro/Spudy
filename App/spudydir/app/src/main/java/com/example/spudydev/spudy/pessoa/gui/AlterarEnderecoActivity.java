package com.example.spudydev.spudy.pessoa.gui;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.example.spudydev.spudy.R;
import com.example.spudydev.spudy.infraestrutura.persistencia.AcessoFirebase;
import com.example.spudydev.spudy.infraestrutura.utils.Auxiliar;
import com.example.spudydev.spudy.perfil.gui.MeuPerfilAlunoActivity;
import com.example.spudydev.spudy.perfil.gui.MeuPerfilProfessorActivity;

public class AlterarEnderecoActivity extends AppCompatActivity {


    private EditText edtAlteraEndereco;
    private EditText edtAlteraComplemento;
    private EditText edtAlteraCidade;
    private Spinner spnAlterarEstado;
    private String tipoConta;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alterar_endereco);

        edtAlteraEndereco = findViewById(R.id.edtAlterarEnderecoPerfil);
        edtAlteraComplemento = findViewById(R.id.edtAlterarComplementoPerfil);
        edtAlteraCidade = findViewById(R.id.edtCidadeRegistro);
        spnAlterarEstado = findViewById(R.id.spnAlteraEstadoPerfil);

        tipoConta = getIntent().getStringExtra("tipoConta");

        Button btnAlterarEndereco = findViewById(R.id.btnAlterarEnderecoPerfil);

        btnAlterarEndereco.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alteraEndereco();
                Auxiliar.criarToast(getApplicationContext(), "Endereço atualizado com sucesso.");
                abrirTelaMeuPerfilActivity();
            }
        });

    }
    public void alteraEndereco(){
        if (!edtAlteraEndereco.getText().toString().trim().isEmpty()){
            AcessoFirebase.getFirebase().child("endereco").child(AcessoFirebase.getUidUsuario()).child("endereco").setValue(edtAlteraEndereco.getText().toString());
        }
        if (!edtAlteraComplemento.getText().toString().trim().isEmpty()){
            AcessoFirebase.getFirebase().child("endereco").child(AcessoFirebase.getUidUsuario()).child("complemento").setValue(edtAlteraComplemento.getText().toString());
        }
        if (!edtAlteraCidade.getText().toString().trim().isEmpty()){
            AcessoFirebase.getFirebase().child("endereco").child(AcessoFirebase.getUidUsuario()).child("cidade").setValue(edtAlteraCidade.getText().toString());
        }
        if (spnAlterarEstado.getSelectedItem()!="Selecione o seu estado"){
            AcessoFirebase.getFirebase().child("endereco").child(AcessoFirebase.getUidUsuario()).child("estado").setValue(spnAlterarEstado.getSelectedItem().toString());
        }
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
