package com.example.spudydev.spudy.entidades.pessoa.gui;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.example.spudydev.spudy.R;
import com.example.spudydev.spudy.entidades.endereco.dominio.Endereco;
import com.example.spudydev.spudy.entidades.usuario.persistencia.EnderecoDAO;
import com.example.spudydev.spudy.infraestrutura.persistencia.AcessoFirebase;
import com.example.spudydev.spudy.infraestrutura.utils.Auxiliar;
import com.example.spudydev.spudy.perfil.gui.MeuPerfilAlunoActivity;
import com.example.spudydev.spudy.perfil.gui.MeuPerfilProfessorActivity;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

public class AlterarEnderecoActivity extends AppCompatActivity {

    private EditText edtAlteraRua;
    private EditText edtAlteraComplemento;
    private EditText edtAlteraCidade;
    private Spinner spnAlterarEstado;
    private String tipoConta;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alterar_endereco);

        edtAlteraRua = findViewById(R.id.edtAlterarRuaPerfil);
        edtAlteraComplemento = findViewById(R.id.edtAlterarComplementoPerfil);
        edtAlteraCidade = findViewById(R.id.edtAlterarCidadePerfil);
        spnAlterarEstado = findViewById(R.id.spnAlteraEstadoPerfil);

        tipoConta = getIntent().getStringExtra("tipoConta");

        Button btnAlterarEndereco = findViewById(R.id.btnAlterarEnderecoPerfil);

        btnAlterarEndereco.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Instanciando a classe EnderecoDAO
                EnderecoDAO enderecoDAO = new EnderecoDAO();
                //Chamando o método para inserir o endereço atualizado
                enderecoDAO.inserirEndereco(criaEndereco());

                Auxiliar.criarToast(getApplicationContext(), "Endereço atualizado com sucesso.");
                abrirTelaMeuPerfilActivity();
            }
        });
        resgatarEndereco();
    }
    //Resgatando o endereço do usuário para setar nos TextViews
    public void resgatarEndereco(){
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
    public void setarCampos(final Endereco endereco){
        edtAlteraRua.setText(endereco.getRua());
        edtAlteraComplemento.setText(endereco.getComplemento());
        edtAlteraCidade.setText(endereco.getCidade());
        setarSpinner(endereco);
    }
    //Setando o spinner com os estados
    public void setarSpinner(Endereco endereco) {
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
    public Endereco criaEndereco(){
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