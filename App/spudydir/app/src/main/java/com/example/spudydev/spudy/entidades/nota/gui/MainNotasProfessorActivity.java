package com.example.spudydev.spudy.entidades.nota.gui;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.example.spudydev.spudy.R;
import com.example.spudydev.spudy.entidades.nota.dominio.Nota;
import com.example.spudydev.spudy.entidades.nota.negocio.NotaServices;
import com.example.spudydev.spudy.infraestrutura.persistencia.AcessoFirebase;
import com.example.spudydev.spudy.infraestrutura.utils.Auxiliar;
import com.example.spudydev.spudy.infraestrutura.utils.VerificaConexao;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

/**
 * The type Main notas professor activity.
 */
public class MainNotasProfessorActivity extends AppCompatActivity {

    private Spinner spnAlunosTurma;
    private EditText edtTituloNota;
    private EditText edtNotaAluno;
    private List<String> listaUidUsuarios = new ArrayList<>();
    private List<String> listaNomesUsuarios = new ArrayList<>();
    private String uidUsuario;

    private VerificaConexao verificaConexao;

    @Override
    protected final void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_notas_professor);

        //Instanciando Spinner/EditText/Button
        spnAlunosTurma = findViewById(R.id.spnAlunosTurma);
        edtNotaAluno = findViewById(R.id.edtAlunoNota);
        edtTituloNota = findViewById(R.id.edtProfessorTituloNota);
        Button btnSalvarNota = findViewById(R.id.btnSalvarNotaAluno);
        //Fim instâncias

        verificaConexao = new VerificaConexao(this);

        spnAlunosTurma.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                uidUsuario = listaUidUsuarios.get(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        btnSalvarNota.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (verificaConexao.estaConectado()) {
                    if (verificarCampos()) {
                        String codigoTurma = getIntent().getStringExtra("codigoTurma");

                        inserirNota(criarNota(), codigoTurma);
                    }
                } else {
                    Auxiliar.criarToast(getApplicationContext(), getString(R.string.sp_conexao_falha));
                }
            }
        });
        carregarAlunosTurma();
    }
    //Método que chama camada de negócio
    private void inserirNota(Nota nota, String codigoTurma){
        if (NotaServices.inserirNotaServices(nota,uidUsuario,codigoTurma)){
            Auxiliar.criarToast(getApplicationContext(), getString(R.string.sp_nota_salva_sucesso));
            edtNotaAluno.setText("");
            edtTituloNota.setText("");
            edtNotaAluno.clearFocus();
        } else {
            Auxiliar.criarToast(getApplicationContext(), getString(R.string.sp_excecao_database));
        }
    }
    //Criando o objeto nota
    private Nota criarNota(){

        Nota nota = new Nota();
        nota.setTitulo(edtTituloNota.getText().toString());
        nota.setValor(Float.parseFloat(edtNotaAluno.getText().toString()));

        return nota;
    }
    //Verificando os campos de título e nota
    private boolean verificarCampos(){
        boolean verificador = true;

        final double maximoNota = 10.0;

        if (edtTituloNota.getText().toString().isEmpty() || edtTituloNota.getText().toString().trim().length() == 0){
            edtTituloNota.setError(getString(R.string.sp_excecao_campo_vazio));
            verificador = false;
        }
        if (edtNotaAluno.getText().toString().isEmpty() || edtNotaAluno.getText().toString().trim().length() == 0){
            edtNotaAluno.setError(getString(R.string.sp_excecao_campo_vazio));
            verificador = false;
        }
        if (Double.parseDouble(edtNotaAluno.getText().toString()) > maximoNota){
            edtNotaAluno.setError(getString(R.string.sp_excecao_nota_aluno));
            verificador = false;
        }
        return verificador;
    }
    //Montando spinner com os alunos da turma
    private void carregarAlunosTurma() {
        AcessoFirebase.getFirebase().addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                getUidUsuariosTurma(dataSnapshot);
                montandoArrayListUsuarios(dataSnapshot);
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }
    //Inserindo em um RetornoLista os UIDs dos usuários
    private void getUidUsuariosTurma(DataSnapshot dataSnapshot){
        Iterable<DataSnapshot> referencia = dataSnapshot.child("turma").child(getIntent().getStringExtra("codigoTurma")).child("usuariosDaTurma").getChildren();
        for (DataSnapshot dataSnapshotChild: referencia){
            if (!dataSnapshotChild.getKey().equals(AcessoFirebase.getUidUsuario())) {
                listaUidUsuarios.add(dataSnapshotChild.getKey());
            }
        }
    }
    //Setando o ListView com o auxílio do ArrayAdapter<>
    private void montandoArrayListUsuarios(DataSnapshot dataSnapshot) {
        //Laço para separar a camada de apresentação ao usuário
        for (String i : listaUidUsuarios) {
            if (!i.equals(AcessoFirebase.getUidUsuario())) {
                String nomeUsuario = dataSnapshot.child("pessoa").child(i).child("nome").getValue(String.class);
                listaNomesUsuarios.add(nomeUsuario + " - Aluno");
            }
        }
        setSpnAlunosTurma();
    }
    //Setando o spinner com os nomes dos alunos
    private void setSpnAlunosTurma(){
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, listaNomesUsuarios);
        spnAlunosTurma.setAdapter(arrayAdapter);
        spnAlunosTurma.setSelection(0);
    }
}