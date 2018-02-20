package com.example.spudydev.spudy.entidades.endereco.gui;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.example.spudydev.spudy.R;
import com.example.spudydev.spudy.entidades.endereco.dominio.Endereco;
import com.example.spudydev.spudy.entidades.endereco.negocio.EnderecoServices;
import com.example.spudydev.spudy.infraestrutura.persistencia.AcessoFirebase;
import com.example.spudydev.spudy.infraestrutura.utils.Auxiliar;
import com.example.spudydev.spudy.infraestrutura.utils.VerificaConexao;
import com.example.spudydev.spudy.perfil.gui.MeuPerfilAlunoActivity;
import com.example.spudydev.spudy.perfil.gui.MeuPerfilProfessorActivity;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

/**
 * The type Alterar endereco activity.
 */
public class AlterarEnderecoActivity extends AppCompatActivity {

    private EditText edtAlteraRua;
    private EditText edtAlteraComplemento;
    private EditText edtAlteraCidade;
    private Spinner spnAlterarEstado;
    private String tipoConta;

    private VerificaConexao verificaConexao;

    @Override
    protected final void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alterar_endereco);

        edtAlteraRua = findViewById(R.id.edtAlterarRuaPerfil);
        edtAlteraComplemento = findViewById(R.id.edtAlterarComplementoPerfil);
        edtAlteraCidade = findViewById(R.id.edtAlterarCidadePerfil);
        spnAlterarEstado = findViewById(R.id.spnAlteraEstadoPerfil);

        verificaConexao = new VerificaConexao(this);

        tipoConta = getIntent().getStringExtra("tipoConta");

        Button btnAlterarEndereco = findViewById(R.id.btnAlterarEnderecoPerfil);

        btnAlterarEndereco.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (verificaConexao.estaConectado()) {
                    //Chamando o método para inserirUsuarioDAO o endereço atualizado
                    alterarEndereco();
                } else {
                    Auxiliar.criarToast(getApplicationContext(), getString(R.string.sp_conexao_falha));
                }
            }
        });
        resgatarEndereco();
    }
    //Método que chama a camada de negócio
    private void alterarEndereco(){
        if (EnderecoServices.alterarEndereco(criaEndereco())) {
            Auxiliar.criarToast(getApplicationContext(), getString(R.string.sp_endereco_alterado_sucesso));
            abrirTelaMeuPerfilActivity();
        } else {
            Auxiliar.criarToast(getApplicationContext(), getString(R.string.sp_excecao_database));
        }
    }
    //Resgatando o endereço do usuário para setar nos TextViews
    private void resgatarEndereco(){
        AcessoFirebase.getFirebase().child("endereco").child(AcessoFirebase.getUidUsuario()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Endereco endereco = dataSnapshot.getValue(Endereco.class);
                setarCampos(endereco);
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }
    //Setando os campos da activity_alterar_endereco
    private void setarCampos(final Endereco endereco){
        edtAlteraRua.setText(endereco.getRua());
        edtAlteraComplemento.setText(endereco.getComplemento());
        edtAlteraCidade.setText(endereco.getCidade());
        setarSpinner(endereco);
    }
    //Setando o spinner com os estados
    private void setarSpinner(Endereco endereco) {
        String[] cEstados = getResources().getStringArray(R.array.spinner_estado);
        int contador = 0;
        for (String i: cEstados){
            if (i.equals(endereco.getEstado())){
                spnAlterarEstado.setSelection(contador);
            }
            contador++;
        }
    }
    //Criando endereço e verificando se os campos estão preenchidos ou não
    private Endereco criaEndereco(){
        Endereco endereco = new Endereco();

        if (edtAlteraRua.getText().toString().trim().length() == 0){
            endereco.setRua("ND");
        }else {
            endereco.setRua(edtAlteraRua.getText().toString());
        }
        if (edtAlteraComplemento.getText().toString().trim().length() == 0){
            endereco.setComplemento("ND");
        }else {
            endereco.setComplemento(edtAlteraComplemento.getText().toString());
        }
        if (edtAlteraCidade.getText().toString().trim().length() == 0){
            endereco.setCidade("ND");
        }else {
            endereco.setCidade(edtAlteraCidade.getText().toString());
        }
        if (spnAlterarEstado.getSelectedItem().equals("Selecione o seu estado")) {
            endereco.setEstado("ND");
        }else {
            endereco.setEstado(spnAlterarEstado.getSelectedItem().toString());
        }
        return endereco;
    }
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